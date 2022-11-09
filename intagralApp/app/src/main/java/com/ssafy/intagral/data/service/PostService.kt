package com.ssafy.intagral.data.service

import com.ssafy.intagral.data.repository.PostRepository
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

    suspend fun publishPost(image: File, tagList: List<String>): Response<ResponseBody>{
        val imageFile = MultipartBody.Part.createFormData("image", image.name, image.asRequestBody("image/jpeg".toMediaTypeOrNull()))
        val hashtags = ArrayList<MultipartBody.Part>()
        for(tag in tagList){
            hashtags.add(MultipartBody.Part.createFormData("hashtags", tag))
        }
        return postRepository.publishPost(imageFile, hashtags)
    }
}