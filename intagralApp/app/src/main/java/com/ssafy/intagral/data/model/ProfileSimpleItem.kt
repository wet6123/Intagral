package com.ssafy.intagral.data.model

data class ProfileSimpleItem (
    val type: ProfileType,
    val name: String,
    val isFollow: Boolean,
    val imgPath: String? = "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png",
): java.io.Serializable