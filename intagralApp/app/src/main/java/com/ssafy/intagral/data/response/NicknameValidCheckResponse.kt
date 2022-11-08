package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NicknameValidCheckResponse (
    @Expose
    @SerializedName(value = "isAvailable")
    val isAvailable: Boolean
)