package com.ssafy.intagral.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.ssafy.intagral.data.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userService: UserService): ViewModel() {

    private var authToken : MutableLiveData<String> = MutableLiveData()

    fun getAuthToken(): MutableLiveData<String>{
        return authToken
    }

    fun login(idToken : String) {
        viewModelScope.launch{
            var json = JsonObject()
            json.addProperty("idToken", idToken)
            val response = userService.login(json)
            if(response.isSuccessful){
                response.body()?.let {
                        authToken.value = it.authToken
                    }
            }else{
                Log.d("RETROFIT /api/user/login", "응답 에러 : ${response.code()}")
            }
        }
    }
}