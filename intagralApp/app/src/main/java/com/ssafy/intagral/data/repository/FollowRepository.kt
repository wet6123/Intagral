package com.ssafy.intagral.data.repository

import com.ssafy.intagral.data.response.OnesFollowResponse
import com.ssafy.intagral.data.response.ToggleFollowResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FollowRepository {
    @POST("/api/follow/user/{nickname}")
    suspend fun toggleFollowUser(@Path("nickname") name: String): Response<ToggleFollowResponse>
    @POST("/api/follow/hashtag/{hashtag}")
    suspend fun toggleFollowHashtag(@Path("hashtag") tag: String): Response<ToggleFollowResponse>
    @GET("/api/follow/list/user")
    suspend fun getOnesFollowingList(@Query("type") type: String, @Query("q") q: String): Response<OnesFollowResponse>
    @GET("/api/follow/list/hashtag")
    suspend fun getHashtagFollowerList(@Query("type") type: String, @Query("q") q: String): Response<OnesFollowResponse>
}