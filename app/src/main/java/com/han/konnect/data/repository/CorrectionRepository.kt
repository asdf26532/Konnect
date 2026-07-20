package com.han.konnect.data.repository

import com.han.konnect.data.dao.CorrectionDao
import com.han.konnect.data.entity.CorrectionEntity
import kotlinx.coroutines.flow.Flow

class CorrectionRepository(private val correctionDao: CorrectionDao) {

    val allCorrections: Flow<List<CorrectionEntity>> = correctionDao.getAllCorrections()

    suspend fun insert(correction: CorrectionEntity) {
        correctionDao.insertCorrection(correction)
    }

    suspend fun delete(correction: CorrectionEntity) {
        correctionDao.deleteCorrection(correction)
    }
}