package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserLoginReponse (
    @Expose
    @SerializedName(value="authToken")
    val authToken: String
)

data class UserProfileResponse (
    @Expose
    @SerializedName(value="type")
    val type: String,
    @Expose
    @SerializedName(value="nickname")
    val nickname: String,
    @Expose
    @SerializedName(value="imgPath")
    val imgPath: String,
    @Expose
    @SerializedName(value="intro")
    val intro: String,
    @Expose
    @SerializedName(value="following")
    val following: Int,
    @Expose
    @SerializedName(value="follower")
    val follower: Int,
    @Expose
    @SerializedName(value="hashtag")
    val hashtag: Int,
    @Expose
    @SerializedName(value="isFollow")
    val isFollow: Boolean
)