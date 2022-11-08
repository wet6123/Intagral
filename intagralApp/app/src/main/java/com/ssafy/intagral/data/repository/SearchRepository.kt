package com.ssafy.intagral.data.repository

import com.google.gson.JsonObject
import com.ssafy.intagral.data.response.searchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchRepository {
    @GET("/api/search")
    suspend fun search(@Query("q") q: String): Response<searchResponse>
    @POST("api/search")
    suspend fun addHashtagSearchCnt(@Body json: JsonObject)
}