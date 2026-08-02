package com.han.konnect.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.han.konnect.data.model.UserProfile
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun saveUserProfile(userProfile: UserProfile): Result<Unit> {
        return try {
            usersCollection.document(userProfile.uid).set(userProfile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String): Result<UserProfile?> {
        return try {
            val snapshot = usersCollection.document(uid).get().await()
            val profile = snapshot.toObject(UserProfile::class.java)
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecommendedUsers(
        currentUid: String,
        targetNativeLanguage: String? = null
    ): Result<List<UserProfile>> {
        return try {
            var query = usersCollection.limit(20)

            if (!targetNativeLanguage.isNull_or_blank()) {
                query = query.whereEqualTo("nativeLanguage", targetNativeLanguage)
            }

            val snapshot = query.get().await()
            val users = snapshot.toObjects(UserProfile::class.java)
                .filter { it.uid != currentUid }

            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}