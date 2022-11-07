package com.ssafy.intagral.data.source.preset

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PresetService {
    @GET(value = "/api/preset/list")
    fun getPresetList(
        @Query(value="type", encoded = true) type: String = "all"
    ): Call<PresetResponse>
}
