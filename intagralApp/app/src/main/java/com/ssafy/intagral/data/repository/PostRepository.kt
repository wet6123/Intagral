package com.ssafy.intagral.data.repository

import com.ssafy.intagral.data.response.PostListResponse
import com.ssafy.intagral.data.response.PostDetailResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Path

interface PostRepository {

    @GET("/api/post/{post_id}")
    suspend fun getPostDetail(
        @Path("post_id") post_id: Int,
    ): Response<PostDetailResponse>

    @POST("/api/post/like/{postId}")
    suspend fun toggleLike(
        @Path("postId") postId: Int
    ): Response<ResponseBody>

    @POST("/api/post/publish")
    @Multipart
    suspend fun publishPost(
        @Part image: MultipartBody.Part,
        @Part hashtags: List<MultipartBody.Part>
    ): Response<ResponseBody>

    @GET("/api/post/list")
    suspend fun getPostList(@Query("type") type: String, @Query("page") page: Int, @Query("q") q: String?): Response<PostListResponse>

    @POST("/api/post/delete/{postId}")
    suspend fun deletePost(@Path("postId") postId: Int): Response<ResponseBody>
}