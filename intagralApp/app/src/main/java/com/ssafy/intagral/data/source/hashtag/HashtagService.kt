package com.ssafy.intagral.data.source.hashtag

import retrofit2.http.GET
import retrofit2.http.Query

interface HashtagService {
    @GET("/api/hashtag/profile")
    suspend fun getHashtagProfile(@Query("q") q: String)
    
}