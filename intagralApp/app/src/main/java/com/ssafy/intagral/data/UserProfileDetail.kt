package com.ssafy.intagral.data

data class UserProfileDetail ( //TODO: 본인인지 확인하는 변수 필요한지 생각해보기
    val nickname: String,
    val follower: Int,
    val following: Int,
    val hashtag: Int,
    val isFollow: Boolean,
    val intro: String
)