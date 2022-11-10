package com.ssafy.intagral.data.service

import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.data.repository.FollowRepository
import com.ssafy.intagral.di.CommonRepository
import retrofit2.Retrofit

class FollowService {
    var commonRepository: Retrofit = CommonRepository.getCommonRepository()
    private var followRepository: FollowRepository

    init {
        followRepository = commonRepository.create(FollowRepository::class.java)
    }

    suspend fun toggleFollowUser(name: String) = followRepository.toggleFollowUser(name)
    suspend fun toggleFollowHashtag(tag: String) = followRepository.toggleFollowHashtag(tag)
    suspend fun getOnesFollowingList(type: String, q: String): ArrayList<ProfileSimpleItem>? {
        val response = followRepository.getOnesFollowingList(type, q)
        val arr = ArrayList<ProfileSimpleItem>()
        if(response.isSuccessful) {
            if(type == "hashtag") {
                response.body()?.data?.let {
                    val len = it.size
                    for (i in 0 .. len-1) {
                        arr.add(ProfileSimpleItem(ProfileType.hashtag, it.get(i).nickname,
                        it.get(i).isFollow, it.get(i).imagePath))
                    }
                }
            } else {
                response.body()?.data?.let {
                    val len = it.size
                    for (i in 0 .. len-1) {
                        arr.add(ProfileSimpleItem(ProfileType.user, it.get(i).nickname,
                            it.get(i).isFollow, it.get(i).imagePath))
                    }
                }
            }
        }
        return arr
    }
    suspend fun getHashtagFollowerList(type: String, q: String): ArrayList<ProfileSimpleItem>? {
        var response = followRepository.getHashtagFollowerList(type, q)
        val arr = ArrayList<ProfileSimpleItem>()
        if(response.isSuccessful) {
            response.body()?.data?.let {
                val len = it.size
                for (i in 0 .. len-1) {
                    arr.add(ProfileSimpleItem(ProfileType.user, it.get(i).nickname,
                        it.get(i).isFollow, it.get(i).imagePath))
                }
            }
        }
        return arr
    }
}