package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ToggleFollowResponse (
    @Expose
    @SerializedName(value="followerCnt")
    val followerCnt: Int,
    @Expose
    @SerializedName(value="isFollow")
    val isFollow: Boolean,
    @Expose
    @SerializedName(value="message")
    val message: String,
    @Expose
    @SerializedName(value="statusCode")
    val statusCode: Int,
)