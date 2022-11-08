package com.ssafy.intagral.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.intagral.data.model.ProfileDetail
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType

const val imgPath = "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"
class ProfileDetailViewModel: ViewModel() {
    private var profileDetail: MutableLiveData<ProfileDetail> = MutableLiveData()
    private var profileSimpleItem: MutableLiveData<ProfileSimpleItem> = MutableLiveData()

    fun getProfileDetail(): MutableLiveData<ProfileDetail> {
        return profileDetail
    }
    fun changeProfileDetail(profileSimple: ProfileSimpleItem) {
        profileDetail.value = ProfileDetail(ProfileType.hashtag, "한유연123415123123", imgPath, 120321, true, 12041,13124,"asdas" )
        //TODO: arrange input data profileSimple -> to profileDetail
    }
}