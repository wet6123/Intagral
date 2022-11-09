package com.ssafy.intagral.data.service

import android.util.Log
import com.google.gson.JsonObject
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.data.repository.SearchRepository
import com.ssafy.intagral.data.response.hashtagSearchResult
import com.ssafy.intagral.data.response.userSearchResult
import com.ssafy.intagral.di.CommonRepository
import retrofit2.Retrofit

class SearchService {
    var commonRepository: Retrofit = CommonRepository.getCommonRepository()
    private var searchRepository: SearchRepository

    init{
        searchRepository = commonRepository.create(SearchRepository::class.java)
    }

    suspend fun search(q: String): ArrayList<ProfileSimpleItem>? {
        var response = searchRepository.search(q)
        if(response.isSuccessful){
            var searchResult: ArrayList<ProfileSimpleItem> = ArrayList()
            response.body()?.let {
                var len = Math.max(it.users.size, it.hashtags.size)
                for (i in 0 .. len-1){
                    if(it.users.size>i) {
                        var user: userSearchResult = it.users.get(i)
                        searchResult.add(ProfileSimpleItem(ProfileType.user, user.name,
                        user.isFollow, user.profileImage))
                    }
                    if(it.hashtags.size>i) {
                        var hashtag: hashtagSearchResult = it.hashtags.get(i)
                        searchResult.add(ProfileSimpleItem(ProfileType.hashtag, hashtag.name,
                        hashtag.isFollow))
                    }
                }
                return searchResult
            }
            return null
        }else{
            Log.d("RETROFIT GET /api/search", "응답 에러 : ${response.code()}")
            return null
        }
    }
    suspend fun addHashtagSearchCnt(json: JsonObject) = searchRepository.addHashtagSearchCnt(json)
}