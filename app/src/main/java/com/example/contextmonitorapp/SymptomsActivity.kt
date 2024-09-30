package com.example.contextmonitorapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.contextmonitorapp.data.VitalSignsSymptomsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SymptomsActivity : AppCompatActivity() {

    private lateinit var symptomsSpinner: Spinner
    private lateinit var ratingBar: RatingBar
    private lateinit var uploadButton: Button
    private lateinit var db: VitalSignsSymptomsDatabase

    private var heartRate: Float = 0.0f
    private var respiratoryRate: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms)

        // Initialize the spinner, rating bar, and button
        symptomsSpinner = findViewById(R.id.symptoms_spinner)
        ratingBar = findViewById(R.id.symptom_rating_bar)
        uploadButton = findViewById(R.id.button_upload_symptoms)

        // Retrieve heart rate and respiratory rate from Intent extras
        heartRate = intent.getFloatExtra("heartRate", 0.0f)
        respiratoryRate = intent.getFloatExtra("respiratoryRate", 0.0f)

        // Load symptoms from the string array in strings.xml
        val symptomsArray = resources.getStringArray(R.array.symptoms_list)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, symptomsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        symptomsSpinner.adapter = adapter

        // Initialize Room database once
        db = Room.databaseBuilder(
            applicationContext,
            VitalSignsSymptomsDatabase::class.java,
            "vital_signs_symptoms_db"
        ).build()

        // Handle upload button click
        uploadButton.setOnClickListener {
            val selectedSymptom = symptomsSpinner.selectedItem.toString()
            val symptomRating = ratingBar.rating.toInt()

            // Basic validation to ensure a rating is given
            if (symptomRating > 0) {
                // Upload the symptoms, heart rate, and respiratory rate to the database
                uploadSymptoms(selectedSymptom, symptomRating)
            } else {
                Toast.makeText(this, "Please rate the symptom before uploading.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadSymptoms(selectedSymptom: String, symptomRating: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = db.vitalSignsSymptomsDao()

            // Create a new VitalSignsSymptoms object
            val symptomsData = VitalSignsSymptoms(
                heartRate = heartRate,
                respiratoryRate = respiratoryRate,
                // Update the respective symptom based on selection, others remain 0
                nausea = if (selectedSymptom == "Nausea") symptomRating else 0,
                headache = if (selectedSymptom == "Headache") symptomRating else 0,
                diarrhea = if (selectedSymptom == "Diarrhea") symptomRating else 0,
                soreThroat = if (selectedSymptom == "Sore Throat") symptomRating else 0,
                fever = if (selectedSymptom == "Fever") symptomRating else 0,
                muscleAche = if (selectedSymptom == "Muscle Ache") symptomRating else 0,
                lossOfSmellOrTaste = if (selectedSymptom == "Loss of Smell or Taste") symptomRating else 0,
                cough = if (selectedSymptom == "Cough") symptomRating else 0,
                shortnessOfBreath = if (selectedSymptom == "Shortness of Breath") symptomRating else 0,
                feelingTired = if (selectedSymptom == "Feeling Tired") symptomRating else 0
            )

            // Insert the symptoms data into the database
            dao.insertVitalSignsSymptoms(symptomsData)

            // Switch back to the main thread to show the confirmation Toast
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SymptomsActivity, "Symptoms uploaded successfully!", Toast.LENGTH_SHORT).show()
                finish()  // Return to the previous screen after upload
            }
        }
    }
}
