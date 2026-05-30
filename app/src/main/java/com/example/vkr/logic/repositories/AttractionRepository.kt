package com.example.vkr.logic.repositories

import android.net.Uri

import com.example.vkr.logic.models.AttractionCreatedResponse
import com.example.vkr.logic.models.AttractionRequest
import com.example.vkr.logic.models.MediaRequest


interface AttractionRepository {
    suspend fun uploadMedia(uris: List<Uri>, type: String): List<MediaRequest>
    suspend fun createAttraction(request: AttractionRequest): AttractionCreatedResponse
}