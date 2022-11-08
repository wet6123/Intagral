package com.ssafy.intagral.data.service

import com.google.gson.JsonObject
import com.ssafy.intagral.data.repository.UserRepository
import com.ssafy.intagral.di.CommonRepository
import okhttp3.RequestBody
import retrofit2.Retrofit

class UserService {
    var commonRepository: Retrofit = CommonRepository.getCommonRepository()
    private var userRepository: UserRepository
    init{
        userRepository = commonRepository.create(UserRepository::class.java)
    }

    suspend fun login(json: JsonObject) = userRepository.login(json)
    suspend fun logout() = userRepository.logout()
    suspend fun getUserProfile(q: String) = userRepository.getUserProfile(q)
    suspend fun updateUserProfile(json: JsonObject) = userRepository.updateUserProfile(json)
    suspend fun updateProfileImg(data: RequestBody) = userRepository.updateProfileImg(data) //??
}