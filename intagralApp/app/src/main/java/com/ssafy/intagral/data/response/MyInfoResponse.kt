package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyInfoResponse(
    @Expose
    @SerializedName(value = "nickname")
    var nickname: String,
    @Expose
    @SerializedName(value = "imgPath")
    var imgPath : String,
    @Expose
    @SerializedName(value = "intro")
    var intro : String
)