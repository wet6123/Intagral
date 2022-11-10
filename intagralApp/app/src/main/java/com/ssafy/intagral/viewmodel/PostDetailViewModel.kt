package com.ssafy.intagral.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.intagral.data.response.PostDetailResponse
import com.ssafy.intagral.data.service.FollowService
import com.ssafy.intagral.data.service.PostService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(private val postService: PostService, private val followService: FollowService): ViewModel() {

    private val postDetail: MutableLiveData<PostDetailResponse> = MutableLiveData()

    fun getPostDetail(): MutableLiveData<PostDetailResponse> {
        return postDetail
    }

    fun toggleWriterFollow(postId: Int){
        postDetail.value?.let {
            viewModelScope.launch {
                val response = followService.toggleFollowUser(it.writer)
                if(response.isSuccessful){
                    fetchPostDetail(postId)
                }else{
                    Log.d("RETROFIT /api/follow/user/${it.writer}", "응답 에러 : ${response.code()}")
                }
            }
        }
    }

    fun togglePostLike(postId: Int){
        viewModelScope.launch {
            val response = postService.toggleLike(postId)
            if(response.isSuccessful){
                fetchPostDetail(postId)
            } else {
                Log.d("RETROFIT /api/post/like", "응답 에러 : ${response.code()}")
            }
        }
    }

    fun fetchPostDetail(postId: Int){
        viewModelScope.launch {
            val response = postService.getPostDetail(postId)

            if(response.isSuccessful){
                response.body()?.let {
                    postDetail.value = it
                }
            } else {
                Log.d("RETROFIT /api/post/detail", "응답 에러 : ${response.code()}")
            }
        }
    }
}