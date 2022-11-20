package com.ssafy.intagral.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.ssafy.intagral.data.model.PresetClassItem
import com.ssafy.intagral.data.response.MyInfoResponse
import com.ssafy.intagral.data.service.PresetService
import com.ssafy.intagral.data.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val service: UserService): ViewModel() {

    private var myInfo : MutableLiveData<MyInfoResponse> = MutableLiveData()

    fun getMyInfo() : MutableLiveData<MyInfoResponse> {
        return myInfo
    }

    fun getInfo() {
        viewModelScope.launch{
            val response = service.getMyInfo()
            if(response.isSuccessful){
                response.body()?.let {
                        myInfo.value = it
                    }
            }else{
                Log.d("RETROFIT /api/user/info", "응답 에러 : ${response.code()}")
            }
        }
    }

}