package com.ssafy.intagral.data.repository

import com.google.gson.JsonObject
import com.ssafy.intagral.data.response.MyInfoResponse
import com.ssafy.intagral.data.response.NicknameValidCheckResponse
import com.ssafy.intagral.data.response.UserLoginReponse
import com.ssafy.intagral.data.response.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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
    @GET(value="/api/user/check")
    suspend fun checkValidName(@Query("nickname") nickname: String): Response<NicknameValidCheckResponse>
    @POST(value="/api/user/profile/info")
    suspend fun updateUserProfile(@Body json: JsonObject): Response<ResponseBody>
    @Multipart
    @POST(value="/api/user/profile/image")
    suspend fun updateProfileImg(@Part data: MultipartBody.Part): Response<ResponseBody>
    @GET(value="/api/user/info")
    suspend fun getMyInfo(): Response<MyInfoResponse>
}