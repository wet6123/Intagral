package com.ssafy.intagral.data.source.hashtag

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HashtagService {
    @GET("/api/hashtag/profile")
    suspend fun getHashtagProfile(@Query("q") q: String): Response<HashtagProfileResponse>

    @GET("api/hashtag/list/hot")
    suspend fun getHotHashtag(): Response<HotHashtagResponse>
}