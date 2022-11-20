package com.ssafy.intagral.data.service

import android.util.Log
import com.ssafy.intagral.data.model.FilterTagItem
import com.ssafy.intagral.data.model.FilterType
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
                    ProfileType.hashtag, it.content!!, it.follower ?:0,it.isFollow ?:false)
                return profileDetail
            }
        } else {
            Log.d("RETROFIT GET /api/hashtag/profile", "응답 에러 : ${response.code()}")
            return null
        }
        return null
    }

    //this returns tag lists that include all, and follow
    suspend fun getHotFiltertag(): ArrayList<FilterTagItem> {
        var filterTagList = ArrayList<FilterTagItem>()
        filterTagList.add(FilterTagItem(FilterType.all, "전체"))
        filterTagList.add(FilterTagItem(FilterType.follow, "팔로우"))
        var response =  hashtagRepository.getHotHashtag()
        if(response.isSuccessful) {
            response.body()?.let {
                for (i in 0..response.body()!!.data.size-1) {
                    filterTagList.add(FilterTagItem(FilterType.hashtag, response.body()!!.data.get(i)))
                }
            }
        } else {
            Log.d("RETROFIT GET /api/hashtag/list/hot", "응답 에러 : ${response.code()}")
        }

        return filterTagList
    }
}