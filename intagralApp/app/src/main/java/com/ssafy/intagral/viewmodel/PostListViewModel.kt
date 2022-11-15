package com.ssafy.intagral.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.intagral.data.model.PostItem
import com.ssafy.intagral.data.response.PostListResponse
import com.ssafy.intagral.data.service.PostService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(private val postService: PostService): ViewModel() {
    private var postList: MutableLiveData<ArrayList<PostItem>> = MutableLiveData()

    enum class StateInfo {
        INIT, //불러오기 시작
        CREATED, //첫 불러오기 완료
        FETCHING, //스크롤 내려서 추가로 불러오기
        LOADED, //추가로 불러오기 완료
        FAILED, //불러오기 실패
        DESTROY, //해당 포스트 리스트 끝
        INACTIVE, //포스트 리스트 페이지 아님
    }

    data class PageInfo (
        var type: String = "all",
        var page: Int = 1,
        var isNext: Boolean = true,
        var state: StateInfo = StateInfo.INACTIVE,
        var q: String?
    )

    private lateinit var pageInfo: PageInfo

    fun getPostList(): MutableLiveData<ArrayList<PostItem>> {
        return postList
    }

    fun getPageInfo(): PageInfo = pageInfo

    //TODO: 여기저기 누르면서 StateInfo따라서 postList 관리
    fun fetchPostList() {
        if(!pageInfo.isNext) return
        viewModelScope.launch {
            val response = postService.getPostList(pageInfo.type, ++pageInfo.page, pageInfo.q)
            if(response.isSuccessful) {
                response.body()?.let {
                    pageInfo.isNext = it.isNext
                    if(it.data.size == 0){
                        return@launch
                    }
                    postList.value = it.data
                }
            }
        }
    }

    fun initPage(type: String, page: Int, q: String?) {
        pageInfo = PageInfo(type, page, true, StateInfo.INIT, q)
        postList.value = ArrayList()

        viewModelScope.launch {
            val response = postService.getPostList(type, page, q)
            if(response.isSuccessful) {
                response.body()?.let {
                    pageInfo.isNext = it.isNext
                    if(it.data.size == 0){
                        return@launch
                    }
                    pageInfo.state = StateInfo.CREATED
                    postList.value = it.data
                }
            }
        }
    }
}