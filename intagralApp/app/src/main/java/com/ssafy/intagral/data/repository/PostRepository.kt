package com.ssafy.intagral.data.repository

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostRepository {

    @POST("/api/post/publish")
    @Multipart
    suspend fun publishPost(
        @Part image: MultipartBody.Part,
        @Part hashtags: List<MultipartBody.Part>
    ): Response<ResponseBody>
}