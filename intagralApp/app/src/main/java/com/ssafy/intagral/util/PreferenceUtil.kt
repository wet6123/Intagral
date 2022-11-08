package com.ssafy.intagral.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("jwtToken", Context.MODE_PRIVATE)
    var token: String?
        //TODO: 토큰 지우기
        get() = prefs.getString("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2IiwiaXNzIjoiaW50YWdyYWwuY29tIiwiZXhwIjoxNjY4OTI2MTMwLCJpYXQiOjE2Njc2MzAxMzB9.85xqJtZn5uXyNhX11yqCsR6mXwhEyUWPWZA9boBKStqUu2moFbFYBMLjPJUE4L9y5z7IyLKee1KQxs3Gi-gwIg")
        //TODO: login view model에서 받아온 토큰 set
        set(value) {prefs.edit().putString("token",value).apply()}
}