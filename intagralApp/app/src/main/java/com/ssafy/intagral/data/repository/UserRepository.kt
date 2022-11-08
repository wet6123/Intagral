package com.ssafy.intagral.data.repository

import com.google.gson.JsonObject
import com.ssafy.intagral.data.response.UserLoginReponse
import com.ssafy.intagral.data.response.UserProfileResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserRepository {
    @POST(value="/api/user/login")
    suspend fun login(@Body json: JsonObject): Response<UserLoginReponse>
    @GET(value="/api/user/logout")
    suspend fun logout()
    @GET(value="/api/user/profile")
    suspend fun getUserProfile(@Query("q") q: String): Response<UserProfileResponse>
    @POST(value="/api/user/profile/info")
    suspend fun updateUserProfile(@Body json: JsonObject)
    @Multipart
    @POST(value="/api/user/profile/image")
    suspend fun updateProfileImg(@Part("data") data: RequestBody)
}