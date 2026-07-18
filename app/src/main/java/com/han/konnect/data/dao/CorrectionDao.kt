package com.han.konnect.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.han.konnect.data.entity.CorrectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CorrectionDao {
    @Query("SELECT * FROM correction_notes ORDER BY timestamp DESC")
    fun getAllCorrections(): Flow<List<CorrectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCorrection(correction: CorrectionEntity)

    @Delete
    suspend fun deleteCorrection(correction: CorrectionEntity)
}