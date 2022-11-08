package com.ssafy.intagral.data.service

import com.ssafy.intagral.data.repository.HashtagRepository
import com.ssafy.intagral.di.CommonRepository
import retrofit2.Retrofit

class HashtagService {

    var commonRepository: Retrofit = CommonRepository.getCommonRepository()
    private var hashtagRepository: HashtagRepository

    init {
        hashtagRepository = commonRepository.create(HashtagRepository::class.java)
    }

    suspend fun getHashtagProfile(q: String) = hashtagRepository.getHashtagProfile(q)
    suspend fun getHotHashtag() = hashtagRepository.getHotHashtag()
}