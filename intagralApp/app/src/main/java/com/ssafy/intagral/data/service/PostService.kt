package com.ssafy.intagral.data.service

import com.ssafy.intagral.data.model.PostItem
import com.ssafy.intagral.data.repository.PostRepository
import com.ssafy.intagral.data.response.PostListResponse
import com.ssafy.intagral.di.CommonRepository
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File


class PostService {

    var commonRepository: Retrofit = CommonRepository.getCommonRepository()
    private var postRepository: PostRepository

    init{
        postRepository = commonRepository.create(PostRepository::class.java)
    }

    suspend fun getPostDetail(postId: Int) = postRepository.getPostDetail(postId)

    suspend fun toggleLike(postId: Int) = postRepository.toggleLike(postId)

    suspend fun publishPost(image: File, tagList: List<String>): Response<ResponseBody>{
        val imageFile = MultipartBody.Part.createFormData("image", image.name, image.asRequestBody("image/jpeg".toMediaTypeOrNull()))
        val hashtags = ArrayList<MultipartBody.Part>()
        for(tag in tagList){
            hashtags.add(MultipartBody.Part.createFormData("hashtags", tag))
        }
        return postRepository.publishPost(imageFile, hashtags)
    }

    suspend fun getPostList(type: String, page: Int, q: String?): Response<PostListResponse> {
        //TODO
        // 1. null일 경우 처리
        // 2. infinite scroll
        return postRepository.getPostList(type, page, q)
    }

    suspend fun deletePost(postId: Int): Response<ResponseBody> = postRepository.deletePost(postId)
}