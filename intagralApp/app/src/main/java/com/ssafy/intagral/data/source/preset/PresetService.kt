package com.ssafy.intagral.data.source.preset

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PresetService {

    @GET(value = "/api/preset/list")
    fun getPresetList(
        @Query(value="type", encoded = true) type: String = "all"
    ): Call<PresetResponse>

    @POST(value = "/api/preset/add")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun addPreset(
        @Body json: JsonObject
    ): Call<ResponseBody>

    @POST(value = "/api/preset/delete")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun deletePreset(
        @Body json: JsonObject
    ): Call<ResponseBody>
}
