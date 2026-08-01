package com.han.konnect.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.han.konnect.data.dao.CorrectionDao
import com.han.konnect.data.entity.CorrectionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
class CorrectionRepository(private val correctionDao: CorrectionDao) {

    private val firestore = FirebaseFirestore.getInstance()

    val allCorrections: Flow<List<CorrectionEntity>> = correctionDao.getAllCorrections()

    suspend fun insert(correction: CorrectionEntity) {
        correctionDao.insertCorrection(correction)
    }

    suspend fun insertCorrection(userUid: String, correction: CorrectionEntity) {
        correctionDao.insertCorrection(correction)

        if (userUid.isNotBlank()) {
            try {
                val firestoreData = hashMapOf(
                    "originalText" to correction.originalText,
                    "correctedText" to correction.correctedText,
                    "reason" to correction.reason,
                    "userName" to correction.userName,
                    "correctionType" to correction.correctionType,
                    "timestamp" to correction.timestamp
                )
                firestore.collection("users")
                    .document(userUid)
                    .collection("corrections")
                    .add(firestoreData)
                    .await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun delete(correction: CorrectionEntity) {
        correctionDao.deleteCorrection(correction)
    }
}