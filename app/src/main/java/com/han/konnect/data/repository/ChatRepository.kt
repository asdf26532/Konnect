package com.han.konnect.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.han.konnect.data.model.FirestoreChatMessage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun sendMessage(roomId: String, message: FirestoreChatMessage): Result<Unit> {
        return try {
            val docRef = firestore.collection("chat_rooms")
                .document(roomId)
                .collection("messages")
                .document()

            val messageWithId = message.copy(messageId = docRef.id)
            docRef.set(messageWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getRealtimeMessages(roomId: String): Flow<List<FirestoreChatMessage>> = callbackFlow {
        val query = firestore.collection("chat_rooms")
            .document(roomId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val messages = snapshot.toObjects(FirestoreChatMessage::class.java)
                trySend(messages)
            }
        }

        awaitClose { listener.remove() }
    }
}