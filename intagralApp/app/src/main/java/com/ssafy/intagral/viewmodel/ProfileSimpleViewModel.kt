package com.ssafy.intagral.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.data.service.FollowService
import com.ssafy.intagral.data.service.SearchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSimpleViewModel @Inject constructor(private val searchService: SearchService, private val followService: FollowService): ViewModel(){
    private var profileSimpleList: MutableLiveData<ArrayList<ProfileSimpleItem>> = MutableLiveData()

    fun getProfileSimpleList(): MutableLiveData<ArrayList<ProfileSimpleItem>>{
        return profileSimpleList
    }

    fun search(q: String) {
        viewModelScope.launch {
            profileSimpleList.value = searchService.search(q) ?: profileSimpleList.value
        }
    }

    suspend fun toggleFollow(profileSimpleItem: ProfileSimpleItem): Boolean{
        val job = viewModelScope.async {
            var result = profileSimpleItem.isFollow
            when(profileSimpleItem.type){
                ProfileType.user -> {
                    val response = followService.toggleFollowUser(profileSimpleItem.name)
                    if(response.isSuccessful){
                        response.body()?.let {
                            result = it.isFollow
                        }
                    }else{
                        Log.d("RETROFIT /api/follow/user", "user follow toggle 실패")
                    }
                }
                ProfileType.hashtag -> {
                    val response = followService.toggleFollowHashtag(profileSimpleItem.name)
                    if(response.isSuccessful){
                        response.body()?.let {
                            result = it.isFollow
                        }
                    }else{
                        Log.d("RETROFIT /api/follow/hashtag", "hashtag follow toggle 실패")
                    }
                }
                else -> {}
            }

            return@async result
        }

        return job.await()
    }

//TODO: array size 0일 때
    fun getOnesFollowList(type: String, q: String) {
        viewModelScope.launch {
            profileSimpleList.value = followService.getOnesFollowingList(type, q)
        }
    }

    fun getHashtagFollowerList(q: String) {
        viewModelScope.launch {
            profileSimpleList.value = followService.getHashtagFollowerList("follower", q)
        }
    }

    fun addSearchCnt(q: String) {
        var json = JsonObject()
        json.addProperty("hashtag", q)
        viewModelScope.launch {
            val response = searchService.addHashtagSearchCnt(json)
            //TODO: API refactor 요청
            if(response.isSuccessful) {
                return@launch
            } else {
                Log.d("Retrofit POST /api/search", "응답 에러 : ${response.code()}")
                return@launch
            }
        }
    }
}