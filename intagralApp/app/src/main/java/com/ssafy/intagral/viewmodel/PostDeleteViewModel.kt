package com.ssafy.intagral.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostDeleteViewModel: ViewModel() {

    private val deletedPostId: MutableLiveData<Int> = MutableLiveData()

    fun getDeletedPostId(): MutableLiveData<Int>{
        return deletedPostId
    }
}