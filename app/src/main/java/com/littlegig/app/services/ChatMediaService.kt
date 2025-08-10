package com.littlegig.app.services

import android.content.ContentResolver
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatMediaService @Inject constructor(
    private val storage: FirebaseStorage,
    private val contentResolver: ContentResolver
) {
    data class UploadProgress(
        val progressPercent: Int,
        val downloadUrl: String? = null
    )

    fun uploadChatMedia(chatId: String, uri: Uri, mimeType: String?): Flow<UploadProgress> = callbackFlow {
        val ext = when {
            mimeType?.contains("image") == true -> ".jpg"
            mimeType?.contains("video") == true -> ".mp4"
            else -> ""
        }
        val fileName = UUID.randomUUID().toString() + ext
        val ref: StorageReference = storage.reference.child("chats/$chatId/$fileName")
        val uploadTask = ref.putFile(uri)
        uploadTask.addOnProgressListener { task ->
            val pct = if (task.totalByteCount > 0) ((100 * task.bytesTransferred) / task.totalByteCount).toInt() else 0
            trySend(UploadProgress(progressPercent = pct))
        }.addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { url ->
                trySend(UploadProgress(progressPercent = 100, downloadUrl = url.toString()))
                close()
            }.addOnFailureListener { e ->
                close(e)
            }
        }.addOnFailureListener { e ->
            close(e)
        }
        awaitClose { /* no-op */ }
    }
}