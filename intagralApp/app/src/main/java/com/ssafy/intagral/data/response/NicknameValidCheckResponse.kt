package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NicknameValidCheckResponse (
    // 0: 괜찮음, 2: 닉네임 중복, 1: 너무 김,
    @Expose
    @SerializedName(value = "isAvailable")
    val isAvailable: Int
)