package com.example.vkr.network.api

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.vkr.network.dto.AttractionCreatedResponse
import com.example.vkr.network.dto.AttractionRequest
import com.example.vkr.network.dto.MediaRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AttractionApiImpl(
    private val context: Context,
    private val mediaApi: MediaApi,
    private val attractionApi: AttractionApi
) {

    /** Грузит несколько файлов одним запросом и оборачивает результат в MediaRequest. */
    suspend fun uploadMedia(uris: List<Uri>, type: String): List<MediaRequest> =
        withContext(Dispatchers.IO) {
            if (uris.isEmpty()) return@withContext emptyList()

            val parts = uris.mapNotNull { it.asMultipart("file") }
            if (parts.isEmpty()) return@withContext emptyList()

            val response = mediaApi.upload(parts)
            if (!response.isSuccessful) {
                throw IOException("Media upload failed: HTTP ${response.code()}")
            }
            val keys = response.body().orEmpty()
            keys.map { MediaRequest(urfRef = it, type = type) }
        }

    suspend fun createAttraction(request: AttractionRequest): AttractionCreatedResponse =
        withContext(Dispatchers.IO) {
            val response = attractionApi.createAttraction(request)
            if (!response.isSuccessful) {
                throw IOException("Create attraction failed: HTTP ${response.code()}")
            }
            response.body() ?: throw IOException("Empty response body")
        }

    private fun Uri.asMultipart(partName: String): MultipartBody.Part? {
        val resolver = context.contentResolver
        val mime = resolver.getType(this) ?: "application/octet-stream"
        val displayName = queryDisplayName() ?: "file_${System.currentTimeMillis()}"
        val bytes = resolver.openInputStream(this)?.use { it.readBytes() } ?: return null
        val body = bytes.toRequestBody(mime.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, displayName, body)
    }

    private fun Uri.queryDisplayName(): String? =
        context.contentResolver.query(this, null, null, null, null)?.use { c ->
            val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (idx >= 0 && c.moveToFirst()) c.getString(idx) else null
        }
}