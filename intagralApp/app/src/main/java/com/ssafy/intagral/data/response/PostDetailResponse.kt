package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostDetailResponse(
    @Expose
    @SerializedName(value = "imgPath")
    val imgPath: String,
    @Expose
    @SerializedName(value = "likeCount")
    val likeCount: Int,
    @Expose
    @SerializedName(value = "isFollow")
    val isFollow: Boolean,
    @Expose
    @SerializedName(value = "isLike")
    val isLike: Boolean,
    @Expose
    @SerializedName(value = "message")
    val message: String,
    @Expose
    @SerializedName(value = "statusCode")
    val statusCode: String,
    @Expose
    @SerializedName(value = "tags")
    val tags: ArrayList<String>,
    @Expose
    @SerializedName(value = "writer")
    val writer: String,
    @Expose
    @SerializedName(value = "writerImgPath")
    val writerImgPath: String,
)