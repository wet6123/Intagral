package com.ssafy.intagral.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.intagral.data.model.PostItem
import com.ssafy.intagral.data.response.PostListResponse
import com.ssafy.intagral.data.service.PostService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(private val postService: PostService): ViewModel() {
    private var postList: MutableLiveData<ArrayList<PostItem>> = MutableLiveData()

    data class PageInfo (
        var type: String = "all",
        var page: Int = 1,
        var isNext: Boolean = true,
        var q: String?
    )

    private lateinit var pageInfo: PageInfo

    fun getPostList(): MutableLiveData<ArrayList<PostItem>> {
        return postList
    }

    fun getPageInfo(): PageInfo = pageInfo

    fun fetchPostList() {
        if(!pageInfo.isNext) return
        viewModelScope.launch {
            val response = postService.getPostList(pageInfo.type, ++pageInfo.page, pageInfo.q)
            if(response.isSuccessful) {
                response.body()?.let {
                    if(it.data.size == 0){
                        pageInfo.isNext = false
                        return@launch
                    }
                    postList.value = it.data
                }
            }
        }
    }

    fun initPage(type: String, page: Int, q: String?) {
        viewModelScope.launch {
            val response = postService.getPostList(type, page, q)
            if(response.isSuccessful) {
                response.body()?.let {
                    if(it.data.size == 0){
                        pageInfo.isNext = false
                        return@launch
                    }
                    postList.value = it.data
                    pageInfo = PageInfo(type, page, it.isNext, q)
                }
            }
        }
    }
}