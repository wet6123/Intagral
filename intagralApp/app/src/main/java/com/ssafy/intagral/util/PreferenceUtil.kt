package com.ssafy.intagral.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("intagral", Context.MODE_PRIVATE)
    var token: String?
        //TODO: 토큰 지우기
        get() = prefs.getString("token", "")
        //TODO: login view model에서 받아온 토큰 set
        set(value) {
            prefs.edit().putString("token",value).apply()
        }
    var nickname: String?
        get() = prefs.getString("nickname", "")
        set(value) {
            prefs.edit().putString("nickname", value).apply()
        }
}