package com.ssafy.intagral.data.source.preset

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PresetResponse(

    @Expose
    @SerializedName(value = "class")
    val classList: ArrayList<String>,

    @Expose
    @SerializedName(value = "data")
    val data: HashMap<String, ArrayList<String>>,
)