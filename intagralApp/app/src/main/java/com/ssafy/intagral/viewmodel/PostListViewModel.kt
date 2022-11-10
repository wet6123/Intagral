package com.ssafy.intagral.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.intagral.data.model.PostItem
import com.ssafy.intagral.data.service.PostService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(private val postService: PostService): ViewModel() {
    private var postList: MutableLiveData<ArrayList<PostItem>> = MutableLiveData()
    private var page: Int = 1
    private var isNext: Boolean = true

    fun getPostList(): MutableLiveData<ArrayList<PostItem>> {
        return postList
    }

    fun fetchPostList(type: String, page: Int, q: String?) {
        println(112123)
        if(!isNext) return
        println(1212312576)
        viewModelScope.launch {
            val response = postService.getPostList(type, page, q)
            println(55)
            if(response.isSuccessful) {
                println(99999)
                response.body()?.let {
                    println(1)
                    if(it.data.size == 0){
                        isNext = false
                        println(2)
                        return@launch
                    }
                    println(3)
                    postList.value = it.data
                    this@PostListViewModel.page = it.page
                    isNext = it.isNext
                }
            }
        }
    }
}