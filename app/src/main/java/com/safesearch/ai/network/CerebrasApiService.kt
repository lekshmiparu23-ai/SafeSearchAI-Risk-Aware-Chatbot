package com.safesearch.ai.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class Message(
    val role: String,
    val content: String
)

data class CerebrasRequest(
    val model: String,
    val messages: List<Message>
)

data class Choice(
    val message: Message
)

data class CerebrasResponse(
    val choices: List<Choice>
)

interface CerebrasApiService {
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") authHeader: String,
        @Body request: CerebrasRequest
    ): CerebrasResponse

    companion object {
        private const val BASE_URL = "https://api.cerebras.ai/v1/"

        fun create(): CerebrasApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CerebrasApiService::class.java)
        }
    }
}
