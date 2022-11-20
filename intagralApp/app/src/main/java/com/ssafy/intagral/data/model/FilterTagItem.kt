package com.ssafy.intagral.data.model

enum class FilterType {
    all,
    hashtag,
    follow,
    user,
}

data class FilterTagItem (
    val type: FilterType,
    val tagContent: String
)