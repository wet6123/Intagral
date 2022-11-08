package com.ssafy.intagral.di

import com.ssafy.intagral.data.source.preset.PresetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonService {
    private val BASE_URL = "https://k7a304.p.ssafy.io"
    private var token: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2IiwiaXNzIjoiaW50YWdyYWwuY29tIiwiZXhwIjoxNjY4OTI2MTMwLCJpYXQiOjE2Njc2MzAxMzB9.85xqJtZn5uXyNhX11yqCsR6mXwhEyUWPWZA9boBKStqUu2moFbFYBMLjPJUE4L9y5z7IyLKee1KQxs3Gi-gwIg"

    private val client: OkHttpClient = OkHttpClient.Builder()
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

    @Singleton
    @Provides
    fun getCommonService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object PresetRepositoryModule {

    @Singleton
    @Provides
    fun providePresetRepository(): PresetRepository = PresetRepository()
}