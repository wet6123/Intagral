package com.ssafy.intagral.data.service

import com.google.gson.JsonObject
import com.ssafy.intagral.data.repository.PresetRepository
import com.ssafy.intagral.di.CommonRepository
import retrofit2.Retrofit

class PresetService {

    var commonRepository: Retrofit = CommonRepository.getCommonRepository()
    private var presetRepository: PresetRepository

    init {
        presetRepository = commonRepository.create(PresetRepository::class.java)
    }

    suspend fun fetchPresetItemList() = presetRepository.getPresetList()

    suspend fun addPresetTag(json: JsonObject) = presetRepository.addPreset(json)

    suspend fun deletePresetTag(json: JsonObject) = presetRepository.deletePreset(json)

}