package com.ssafy.intagral.data

data class ProfileSimpleItem (
    val profileSimpleId: Int,
    val profileSimpleType: String, //user, hashtag
    val profileSimpleImgPath: String,
    val profileSimpleNickName: String,
    val profileSimpleIsFollow: Boolean
)