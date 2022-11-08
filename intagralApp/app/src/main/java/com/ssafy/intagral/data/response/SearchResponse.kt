package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class userSearchResult (
    @Expose
    @SerializedName(value = "name")
    val name: String,
    @Expose
    @SerializedName(value = "profileImage")
    val profileImage: String,
    @Expose
    @SerializedName(value = "isFollow")
    val isFollow: Boolean
)

data class hashtagSearchResult (
    @Expose
    @SerializedName(value = "name")
    val name: String,
    @Expose
    @SerializedName(value = "isFollow")
    val isFollow: Boolean
)

data class searchResponse (
    @Expose
    @SerializedName(value = "users")
    val users: ArrayList<userSearchResult>,
    @Expose
    @SerializedName(value = "hashtags")
    val hashtags: ArrayList<hashtagSearchResult>
)