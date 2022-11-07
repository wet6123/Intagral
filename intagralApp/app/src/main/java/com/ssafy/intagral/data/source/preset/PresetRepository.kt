package com.ssafy.intagral.data.source.preset

import com.google.gson.JsonObject
import com.ssafy.intagral.di.CommonService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject

class PresetRepository {

    @Inject
    val commonService: Retrofit = CommonService.getCommonService()
    private var presetService: PresetService

    init {
        presetService = commonService.create(PresetService::class.java)
    }

    suspend fun fetchPresetItemList(): Call<PresetResponse> = presetService.getPresetList()

    suspend fun addPresetTag(json: JsonObject): Call<ResponseBody> = presetService.addPreset(json)

    suspend fun deletePresetTag(json: JsonObject): Call<ResponseBody> = presetService.deletePreset(json)

}