package com.example.cbink

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RasaService {
    @POST("/webhooks/rest/webhook")
    fun sendMessage(@Body messageRequest: MessageRequest): Call<List<MessageResponse>>
}

data class MessageRequest(val sender: String, val message: String)
data class MessageResponse(val text: String)
