package com.ssafy.intagral.data.source.preset

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PresetRepository {
    private val BASE_URL = "https://k7a304.p.ssafy.io"
    var token: String = ""

    private var presetService: PresetService

    init {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor {
                // Request
                val request = it.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                // Response
                val response = it.proceed(request)
                response
            }
            .build()

        presetService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PresetService::class.java)
    }

    suspend fun fetchPresetItemList(): Call<PresetResponse> = presetService.getPresetList()
}