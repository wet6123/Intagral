package com.ssafy.intagral.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PresetResponse(

    @Expose
    @SerializedName(value = "class")
    val classList: ArrayList<String>,

    @Expose
    @SerializedName(value = "classKor")
    val classListKor: Map<String, String>,

    @Expose
    @SerializedName(value = "data")
    val data: HashMap<String, ArrayList<String>>,
)