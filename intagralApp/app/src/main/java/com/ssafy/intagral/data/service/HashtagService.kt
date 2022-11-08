package com.ssafy.intagral.data.service

import android.util.Log
import com.ssafy.intagral.data.model.ProfileDetail
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.data.repository.HashtagRepository
import com.ssafy.intagral.di.CommonRepository
import retrofit2.Retrofit

class HashtagService {

    var commonRepository: Retrofit = CommonRepository.getCommonRepository()
    private var hashtagRepository: HashtagRepository

    init {
        hashtagRepository = commonRepository.create(HashtagRepository::class.java)
    }

    suspend fun getHashtagProfile(q: String): ProfileDetail? {
        var response = hashtagRepository.getHashtagProfile(q)
        if(response.isSuccessful){
            var profileDetail: ProfileDetail
            response.body()?.let {
                profileDetail = ProfileDetail(
                    ProfileType.user, it.content!!, it.follower ?:0,it.isFollow ?:false)
                return profileDetail
            }
        } else {
            Log.d("RETROFIT GET /api/hashtag/profile", "응답 에러 : ${response.code()}")
            return null
        }
        return null
    }
    suspend fun getHotHashtag() = hashtagRepository.getHotHashtag()
}