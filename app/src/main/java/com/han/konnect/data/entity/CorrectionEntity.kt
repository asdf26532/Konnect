package com.han.konnect.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "correction_notes")
data class CorrectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalText: String,
    val correctedText: String,
    val reason: String,
    val userName: String,
    val timestamp: Long
)