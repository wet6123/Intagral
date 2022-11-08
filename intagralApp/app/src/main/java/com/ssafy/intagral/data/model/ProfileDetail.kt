package com.ssafy.intagral.data.model

data class ProfileDetail ( //TODO: 본인인지 확인하는 변수 필요한지 생각해보기
    val type: ProfileType,
    val name: String, //user nickname or hashtag content
    val follower: Int,
    val isFollow: Boolean,
    val profileImg: String? = "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png",
    val following: Int? = null,
    val hashtag: Int? = null,
    val intro: String? = null,
):java.io.Serializable