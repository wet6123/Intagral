package com.ssafy.intagral.data.repository

import com.google.gson.JsonObject
import com.ssafy.intagral.data.response.PresetResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface PresetRepository {

    @GET(value = "/api/preset/list")
    suspend fun getPresetList(
        @Query(value="type", encoded = true) type: String = "all"
    ): Response<PresetResponse>

    @POST(value = "/api/preset/add")
    @Headers("Content-Type:application/json; charset=UTF-8")
    suspend fun addPreset(
        @Body json: JsonObject
    ): Response<ResponseBody>

    @POST(value = "/api/preset/delete")
    @Headers("Content-Type:application/json; charset=UTF-8")
    suspend fun deletePreset(
        @Body json: JsonObject
    ): Response<ResponseBody>
}
