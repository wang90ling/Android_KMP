/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.fruitties.network.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description Token 本地存储管理器
 *
 * 跨平台 Token 存储实现
 * 实际存储需要配合平台特定实现 (DataStore / Keychain 等)
 */
class TokenStorage {

    private var _token: String? = null
    private var _refreshToken: String? = null

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun saveToken(token: String, refreshToken: String? = null) {
        _token = token
        _refreshToken = refreshToken
        _isLoggedIn.value = true
    }

    fun getToken(): String? = _token

    fun getRefreshToken(): String? = _refreshToken

    fun clearToken() {
        _token = null
        _refreshToken = null
        _isLoggedIn.value = false
    }

    fun hasToken(): Boolean = _token != null
}

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 网络状态管理器
 */
object NetworkState {

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _networkType = MutableStateFlow(NetworkType.UNKNOWN)
    val networkType: StateFlow<NetworkType> = _networkType.asStateFlow()

    fun setConnected(connected: Boolean) {
        _isConnected.value = connected
    }

    fun setNetworkType(type: NetworkType) {
        _networkType.value = type
    }
}

enum class NetworkType {
    WIFI,
    CELLULAR,
    ETHERNET,
    UNKNOWN
}
