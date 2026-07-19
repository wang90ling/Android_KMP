package com.example.fruitties.android.ui.login

import android.content.Context
import android.content.SharedPreferences

class LoginStateManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("login_state", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean("is_logged_in", false)
    }

    fun login() {
        preferences.edit().putBoolean("is_logged_in", true).apply()
    }

    fun logout() {
        preferences.edit().putBoolean("is_logged_in", false).apply()
    }
}