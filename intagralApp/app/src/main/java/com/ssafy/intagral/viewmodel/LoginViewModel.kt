package com.ssafy.intagral.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.ssafy.intagral.IntagralApplication
import com.ssafy.intagral.data.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userService: UserService): ViewModel() {

    private var authToken : MutableLiveData<String> = MutableLiveData()
    private var isLogin: MutableLiveData<Boolean> = MutableLiveData()

    fun getAuthToken(): MutableLiveData<String>{
        return authToken
    }

    fun getIsLogin() : MutableLiveData<Boolean> {
        return isLogin
    }

    fun login(idToken : String) {
        viewModelScope.launch{
            var json = JsonObject()
            json.addProperty("idToken", idToken)
            val response = userService.login(json)
            if(response.isSuccessful){
                response.body()?.let {
                    authToken.value = it.authToken
                    isLogin.value = true
                }
            }else{
                Log.d("RETROFIT /api/user/login", "응답 에러 : ${response.code()}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val response = userService.logout()
            if(response.isSuccessful){
                isLogin.value = false
            } else{
                isLogin.value = false
                IntagralApplication.prefs.token == "EXPIRED"
                Log.d("RETROFIT /api/user/logout", "응답 에러 : ${response.code()}")
            }
        }
    }
}