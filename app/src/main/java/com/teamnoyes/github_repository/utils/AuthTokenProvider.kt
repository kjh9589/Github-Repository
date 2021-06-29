package com.teamnoyes.github_repository.utils

import android.content.Context
import androidx.preference.PreferenceManager

/*
PreferenceManager.getDefaultSharedPreferences()
별도의 파일명을 명시하지 않으므로 기본으로 앱의 패키지명을 파일명으로 사용한다.
 */

class AuthTokenProvider(private val context: Context) {

    companion object {

        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    fun updateToken(token: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
    }

    val token: String?
        get() = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_AUTH_TOKEN, null)

}