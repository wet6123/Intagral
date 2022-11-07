package com.ssafy.intagral.di

import com.ssafy.intagral.data.source.preset.PresetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresetRepositoryModule {

    @Singleton
    @Provides
    fun providePresetRepository(): PresetRepository = PresetRepository()
}