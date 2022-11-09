package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

var imgPath = "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"

data class OnesFollow (
    @Expose
    @SerializedName(value = "nickname")
    val nickname: String,
    @Expose
    @SerializedName(value = "isFollow")
    val isFollow: Boolean,
    @Expose
    @SerializedName(value = "imagePath")
    val imagePath: String? = imgPath
)

data class OnesFollowResponse (
    @Expose
    @SerializedName(value="data")
    val data: ArrayList<OnesFollow>
)
