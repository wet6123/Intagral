package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HashtagProfileResponse(
    @Expose
    @SerializedName(value = "type")
    val type: String,
    @Expose
    @SerializedName(value = "content")
    val content: String,
    @Expose
    @SerializedName(value = "follower")
    val follower: Int,
    @Expose
    @SerializedName(value = "isFollow")
    val isFollow: Boolean
)

data class HotHashtagResponse(
    @Expose
    @SerializedName(value="data")
    val data: ArrayList<String>
)