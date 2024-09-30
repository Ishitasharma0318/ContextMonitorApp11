package com.example.contextmonitorapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vital_signs_symptoms")
data class VitalSignsSymptoms(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // Primary key
    val heartRate: Float,
    val respiratoryRate: Float,
    val nausea: Int = 0,
    val headache: Int = 0,
    val diarrhea: Int = 0,
    val soreThroat: Int = 0,
    val fever: Int = 0,
    val muscleAche: Int = 0,
    val lossOfSmellOrTaste: Int = 0,
    val cough: Int = 0,
    val shortnessOfBreath: Int = 0,
    val feelingTired: Int = 0
)
