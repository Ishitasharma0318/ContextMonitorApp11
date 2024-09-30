package com.example.contextmonitorapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button

import android.widget.TextView


import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.contextmonitorapp.data.VitalSignsSymptomsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import kotlin.math.pow
import android.util.Log
import com.opencsv.CSVReader
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var heartRateTextView: TextView
    private lateinit var respiratoryRateTextView: TextView
    private val heartRateReader = HeartRateReader()

    // Register the video picker result
    private val pickVideoLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            calculateHeartRateFromUri(it)  // Call heart rate calculation function
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize TextViews
        heartRateTextView = findViewById(R.id.textview_heart_rate_result)
        respiratoryRateTextView = findViewById(R.id.textview_respiratory_rate_result)

        // Initialize buttons
        val heartRateButton: Button = findViewById(R.id.button_heart_rate)
        val respiratoryRateButton: Button = findViewById(R.id.button_respiratory_rate)
        val uploadSignsButton: Button = findViewById(R.id.button_upload_signs)

        // Initialize Room database
        val db = Room.databaseBuilder(
            applicationContext,
            VitalSignsSymptomsDatabase::class.java,
            "vital_signs_symptoms_db"
        ).build()

        // Check for permissions
        checkAndRequestPermissions()

        // Heart Rate Button Action
        heartRateButton.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                pickVideoLauncher.launch("video/*") // Launch video picker when button is clicked
            } else {
                // Fallback for Android versions below 13
                CoroutineScope(Dispatchers.IO).launch {
                    val heartRate = heartRateReader.read(this@MainActivity, this@MainActivity)
                    withContext(Dispatchers.Main) {
                        val heartRateValue = heartRate ?: 0
                        heartRateTextView.text = getString(R.string.heart_rate_bpm, heartRateValue)
                    }
                }
            }
        }

        // Respiratory Rate Button Action

        respiratoryRateButton.setOnClickListener {
            // Call the suspend function within a coroutine
            CoroutineScope(Dispatchers.IO).launch {
                val respiratoryRate = calculateRespiratoryRateFromCSV()

                // Switch back to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    respiratoryRateTextView.text = getString(R.string.respiratory_rate_bpm, respiratoryRate)
                }
            }
        }


        // Upload Signs Button Action
        uploadSignsButton.setOnClickListener {
            insertDataToDatabase(db)
            val heartRate = heartRateTextView.text.toString().toFloatOrNull() ?: 0f
            val respiratoryRate = respiratoryRateTextView.text.toString().toFloatOrNull() ?: 0f
            val intent = Intent(this, SymptomsActivity::class.java).apply {
                putExtra("heartRate", heartRate)
                putExtra("respiratoryRate", respiratoryRate)
            }
            startActivity(intent)
        }
    }

    // Check and Request Permissions
    private fun checkAndRequestPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permissionsToRequest = arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO
            )
            if (!hasPermissions(permissionsToRequest)) {
                ActivityCompat.requestPermissions(this, permissionsToRequest, 1)
            }
        } else {
            val permissionsToRequest = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (!hasPermissions(permissionsToRequest)) {
                ActivityCompat.requestPermissions(this, permissionsToRequest, 1)
            }
        }
    }

    // Helper function to check permissions
    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                pickVideoLauncher.launch("video/*")
            }
        } else {
            heartRateTextView.text = getString(R.string.permission_denied)
        }
    }

    // Calculate heart rate using the selected video URI
    private fun calculateHeartRateFromUri(videoUri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val heartRate = heartRateReader.readFromUri(this@MainActivity, videoUri)
            withContext(Dispatchers.Main) {
                val heartRateValue = heartRate ?: 0
                heartRateTextView.text = getString(R.string.heart_rate_bpm, heartRateValue)
            }
        }
    }

    // Insert data into the Room database
    private fun insertDataToDatabase(db: VitalSignsSymptomsDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Capture heart rate and respiratory rate
                val heartRate = heartRateTextView.text.toString().toFloatOrNull() ?: 0f
                val respiratoryRate = respiratoryRateTextView.text.toString().toFloatOrNull() ?: 0f

                // Log the values to verify correct capturing
                Log.d("Database", "Inserting Heart Rate: $heartRate, Respiratory Rate: $respiratoryRate")

                // Insert the data into the Room database
                val symptoms = VitalSignsSymptoms(
                    heartRate = heartRate,
                    respiratoryRate = respiratoryRate
                )
                db.vitalSignsSymptomsDao().insertVitalSignsSymptoms(symptoms)

                // Provide success feedback to the user on the main thread
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Data saved successfully", Toast.LENGTH_SHORT).show()
                }
                Log.d("Database", "Data inserted successfully into database")

            } catch (e: Exception) {
                // Handle any potential errors, log the issue, and inform the user
                Log.e("Database", "Error inserting data: ${e.localizedMessage}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Failed to save data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    // Function to load CSV data from assets folder

    private suspend fun loadCSVData(fileName: String): MutableList<Float> {
        return withContext(Dispatchers.IO) {  // Non-blocking I/O operation
            val resultList = mutableListOf<Float>()
            try {
                val assetManager = assets
                val inputStream = assetManager.open(fileName)
                val reader = CSVReader(InputStreamReader(inputStream))

                reader.forEach { row ->
                    if (row.isNotEmpty()) {
                        resultList.add(row[0].toFloat())
                    }
                }
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CSV Error", "Error reading CSV file: $fileName")
            }
            resultList
        }
    }

    // Updated calculateRespiratoryRateFromCSV to be a suspend function
    private suspend fun calculateRespiratoryRateFromCSV(): Int {
        val accelValuesX = loadCSVData("CSVBreatheX.csv")
        val accelValuesY = loadCSVData("CSVBreatheY.csv")
        val accelValuesZ = loadCSVData("CSVBreatheZ.csv")
        return respiratoryRateCalculator(accelValuesX, accelValuesY, accelValuesZ)
    }


    // Respiratory rate calculation logic
    private fun respiratoryRateCalculator(
        accelValuesX: MutableList<Float>, accelValuesY: MutableList<Float>, accelValuesZ: MutableList<Float>
    ): Int {
        var previousValue = 10f
        var currentValue: Float
        var k = 0
        for (i in 11 until accelValuesY.size) {
            currentValue = kotlin.math.sqrt(
                accelValuesZ[i].toDouble().pow(2.0) + accelValuesX[i].toDouble().pow(2.0) +
                        accelValuesY[i].toDouble().pow(2.0)
            ).toFloat()
            if (kotlin.math.abs(previousValue - currentValue) > 0.15) {
                k++
            }
            previousValue = currentValue
        }
        val ret = (k.toDouble() / 45.00)
        return (ret * 30).toInt()  // Respiratory rate per minute
    }
}
