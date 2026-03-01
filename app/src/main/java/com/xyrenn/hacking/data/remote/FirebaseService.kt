package com.xyrenn.hacking.data.remote

import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseService @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Realtime Database operations
    suspend fun writeData(path: String, data: Any) {
        database.getReference(path).setValue(data).await()
    }

    suspend fun updateData(path: String, data: Map<String, Any>) {
        database.getReference(path).updateChildren(data).await()
    }

    suspend fun deleteData(path: String) {
        database.getReference(path).removeValue().await()
    }

    suspend fun readDataOnce(path: String): DataSnapshot? {
        return database.getReference(path).get().await()
    }

    fun observeData(path: String): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        database.getReference(path).addValueEventListener(listener)
        awaitClose { database.getReference(path).removeEventListener(listener) }
    }

    // Firebase Cloud Messaging
    suspend fun getFCMToken(): String {
        return FirebaseMessaging.getInstance().token.await()
    }

    suspend fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).await()
    }

    suspend fun unsubscribeFromTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).await()
    }

    // Firebase Storage
    suspend fun uploadFile(path: String, data: ByteArray): String {
        val ref = storage.reference.child(path)
        ref.putBytes(data).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun downloadFile(path: String): ByteArray {
        val ref = storage.reference.child(path)
        return ref.getBytes(Long.MAX_VALUE).await()
    }

    suspend fun deleteFile(path: String) {
        storage.reference.child(path).delete().await()
    }
}