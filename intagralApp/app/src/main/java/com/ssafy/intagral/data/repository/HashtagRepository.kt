package com.ssafy.intagral.data.repository

import com.ssafy.intagral.data.service.HashtagService
import com.ssafy.intagral.di.CommonService
import retrofit2.Retrofit

class HashtagRepository {

    var commonService: Retrofit = CommonService.getCommonService()
    private var hashtagService: HashtagService

    init {
        hashtagService = commonService.create(HashtagService::class.java)
    }

    suspend fun getHashtagProfile(q: String) = hashtagService.getHashtagProfile(q)

}