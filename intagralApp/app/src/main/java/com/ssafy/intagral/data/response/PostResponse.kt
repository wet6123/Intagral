package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ssafy.intagral.data.model.PostItem

data class PostListResponse (
    @Expose
    @SerializedName(value = "data")
    val data: ArrayList<PostItem>,
    @Expose
    @SerializedName(value = "page")
    val page: Int,
    @Expose
    @SerializedName(value = "isNext")
    val isNext: Boolean
)