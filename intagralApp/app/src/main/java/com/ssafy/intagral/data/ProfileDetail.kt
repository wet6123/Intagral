package com.ssafy.intagral.data

data class ProfileDetail ( //TODO: 본인인지 확인하는 변수 필요한지 생각해보기
    val type: Enum<ProfileType>,
    val name: String, //user nickname or hashtag content
    val profileImg: String,
    val follower: Int,
    val isFollow: Boolean,
    val following: Int? = null,
    val hashtag: Int? = null,
    val intro: String? = null,
):java.io.Serializable