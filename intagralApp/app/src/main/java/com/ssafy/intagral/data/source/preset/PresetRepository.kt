package com.ssafy.intagral.data.source.preset

import com.google.gson.JsonObject
import com.ssafy.intagral.di.CommonService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

class PresetRepository {

    var commonService: Retrofit = CommonService.getCommonService()
    private var presetService: PresetService

    init {
        presetService = commonService.create(PresetService::class.java)
    }

    suspend fun fetchPresetItemList() = presetService.getPresetList()

    suspend fun addPresetTag(json: JsonObject) = presetService.addPreset(json)

    suspend fun deletePresetTag(json: JsonObject) = presetService.deletePreset(json)

}