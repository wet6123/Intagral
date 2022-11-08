package com.ssafy.intagral.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.ssafy.intagral.data.model.ProfileDetail
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.data.service.HashtagService
import com.ssafy.intagral.data.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

const val imgPath = "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(private val hashtagService: HashtagService, private val userService: UserService): ViewModel() {
    private var profileDetail: MutableLiveData<ProfileDetail> = MutableLiveData()

    fun getProfileDetail(): MutableLiveData<ProfileDetail> {
        return profileDetail
    }
    fun changeProfileDetail(profileSimple: ProfileSimpleItem) {
        viewModelScope.launch {
            var q: String = profileSimple.name
            if(profileSimple.type == ProfileType.user) {
                profileDetail.value = userService.getUserProfile(q) ?:profileDetail.value
            } else {
                profileDetail.value = hashtagService.getHashtagProfile(q) ?:profileDetail.value
            }
        }
    }
}