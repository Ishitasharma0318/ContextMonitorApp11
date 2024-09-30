package com.example.contextmonitorapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contextmonitorapp.VitalSignsSymptoms

@Database(entities = [VitalSignsSymptoms::class], version = 1, exportSchema = false)
abstract class VitalSignsSymptomsDatabase : RoomDatabase() {

    abstract fun vitalSignsSymptomsDao(): VitalSignsSymptomsDao

    companion object {
        @Volatile
        private var INSTANCE: VitalSignsSymptomsDatabase? = null

        // Singleton pattern to get a single instance of the database
        fun getDatabase(context: Context): VitalSignsSymptomsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitalSignsSymptomsDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()  // Optional: Allows destruction and recreation of the DB if no migration is provided
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private const val DATABASE_NAME: String = "vital_signs_symptoms_db"
    }
}
