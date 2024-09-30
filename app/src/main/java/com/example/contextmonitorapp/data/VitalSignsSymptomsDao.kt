package com.example.contextmonitorapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.contextmonitorapp.VitalSignsSymptoms

@Dao
interface VitalSignsSymptomsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVitalSignsSymptoms(vitalSignsSymptoms: VitalSignsSymptoms)
}
