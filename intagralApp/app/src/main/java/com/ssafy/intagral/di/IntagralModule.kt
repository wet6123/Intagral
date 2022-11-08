package com.ssafy.intagral.di

import com.ssafy.intagral.data.service.PresetService
import com.ssafy.intagral.util.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonRepository {
    private val BASE_URL = "https://k7a304.p.ssafy.io"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .addInterceptor(AuthInterceptor())
        .build()

    @Singleton
    @Provides
    fun getCommonRepository(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object PresetServiceModule {

    @Singleton
    @Provides
    fun providePresetService(): PresetService = PresetService()
}
