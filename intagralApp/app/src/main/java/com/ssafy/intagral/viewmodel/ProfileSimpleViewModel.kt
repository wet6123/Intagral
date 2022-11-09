package com.ssafy.intagral.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.service.FollowService
import com.ssafy.intagral.data.service.SearchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSimpleViewModel @Inject constructor(private val searchService: SearchService, private val followService: FollowService): ViewModel(){
    private var profileSimpleList: MutableLiveData<ArrayList<ProfileSimpleItem>> = MutableLiveData(ArrayList())

    fun getProfileSimpleList(): MutableLiveData<ArrayList<ProfileSimpleItem>>{
        return profileSimpleList
    }

    fun search(q: String) {
        viewModelScope.launch {
            profileSimpleList.value = searchService.search(q) ?: profileSimpleList.value
        }
    }

    //TODO: use in post detail
    //      just get first element of list
    fun getProfileSimple(q: String) {

    }

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
}