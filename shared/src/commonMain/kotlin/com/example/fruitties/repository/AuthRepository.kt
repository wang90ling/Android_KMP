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
package com.example.fruitties.repository

import com.example.fruitties.model.LoginResponse
import com.example.fruitties.model.RefreshTokenResponse
import com.example.fruitties.model.RegisterRequest
import com.example.fruitties.model.ForgetPasswordRequest
import com.example.fruitties.model.SmsType
import com.example.fruitties.model.UserInfo
import com.example.fruitties.network.AuthService
import com.example.fruitties.network.core.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 登录/认证数据仓库
 *
 * 职责:
 * - 管理用户登录状态
 * - 处理验证码发送/验证逻辑
 * - 管理 Token 存储
 * - 提供登录状态流
 */
class AuthRepository(
    private val authService: AuthService
) {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.LoggedOut)
    val loginState: Flow<LoginState> = _loginState.asStateFlow()

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: Flow<UserInfo?> = _userInfo.asStateFlow()

    private var _currentToken: String? = null
    private var _refreshToken: String? = null

    /**
     * 发送验证码
     * @param phone 手机号
     * @param type 验证码类型
     */
    suspend fun sendSmsCode(phone: String, type: SmsType = SmsType.LOGIN): Result<Unit> {
        return when (val result = authService.sendSmsCode(phone, type.value)) {
            is NetworkResult.Success -> {
                if (result.data.isSuccess) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(result.data.message))
                }
            }
            is NetworkResult.Error -> Result.failure(Exception(result.message))
            is NetworkResult.Loading -> Result.failure(Exception("Loading"))
        }
    }

    /**
     * 验证验证码
     * @param phone 手机号
     * @param code 验证码
     */
    suspend fun verifySmsCode(phone: String, code: String): Result<Unit> {
        return when (val result = authService.verifySmsCode(phone, code)) {
            is NetworkResult.Success -> {
                if (result.data.isSuccess) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(result.data.message))
                }
            }
            is NetworkResult.Error -> Result.failure(Exception(result.message))
            is NetworkResult.Loading -> Result.failure(Exception("Loading"))
        }
    }

    /**
     * 验证码登录
     * @param phone 手机号
     * @param code 验证码
     * @param deviceId 设备ID
     * @param deviceType 设备类型
     */
    suspend fun smsLogin(
        phone: String,
        code: String,
        deviceId: String? = null,
        deviceType: Int = 1
    ): Result<LoginResponse> {
        _loginState.value = LoginState.Loading

        return when (val result = authService.smsLogin(phone, code, deviceId, deviceType)) {
            is NetworkResult.Success -> {
                val response = result.data
                if (response.isSuccess && response.data != null) {
                    val loginResponse = response.data
                    _currentToken = loginResponse.token
                    _refreshToken = loginResponse.refreshToken
                    _userInfo.value = loginResponse.userInfo
                    _loginState.value = LoginState.LoggedIn(loginResponse)
                    Result.success(loginResponse)
                } else {
                    _loginState.value = LoginState.Error(response.message)
                    Result.failure(Exception(response.message))
                }
            }
            is NetworkResult.Error -> {
                _loginState.value = LoginState.Error(result.message)
                Result.failure(Exception(result.message))
            }
            is NetworkResult.Loading -> {
                Result.failure(Exception("Loading"))
            }
        }
    }

    /**
     * 刷新Token
     */
    suspend fun refreshToken(): Result<RefreshTokenResponse> {
        val refreshToken = _refreshToken ?: return Result.failure(Exception("No refresh token"))

        return when (val result = authService.refreshToken(refreshToken)) {
            is NetworkResult.Success -> {
                val response = result.data
                if (response.isSuccess && response.data != null) {
                    val tokenResponse = response.data
                    _currentToken = tokenResponse.token
                    _refreshToken = tokenResponse.refreshToken
                    Result.success(tokenResponse)
                } else {
                    _loginState.value = LoginState.LoggedOut
                    Result.failure(Exception(response.message))
                }
            }
            is NetworkResult.Error -> {
                _loginState.value = LoginState.LoggedOut
                Result.failure(Exception(result.message))
            }
            is NetworkResult.Loading -> Result.failure(Exception("Loading"))
        }
    }

    /**
     * 获取用户信息
     */
    suspend fun fetchUserProfile(): Result<UserInfo> {
        return when (val result = authService.getUserProfile()) {
            is NetworkResult.Success -> {
                val response = result.data
                if (response.isSuccess && response.data != null) {
                    _userInfo.value = response.data
                    Result.success(response.data)
                } else {
                    Result.failure(Exception(response.message))
                }
            }
            is NetworkResult.Error -> Result.failure(Exception(result.message))
            is NetworkResult.Loading -> Result.failure(Exception("Loading"))
        }
    }

    /**
     * 更新用户信息
     */
    suspend fun updateProfile(
        nickname: String? = null,
        avatar: String? = null,
        gender: Int? = null,
        email: String? = null
    ): Result<UserInfo> {
        return when (val result = authService.updateProfile(nickname, avatar, gender, email)) {
            is NetworkResult.Success -> {
                val response = result.data
                if (response.isSuccess && response.data != null) {
                    _userInfo.value = response.data
                    Result.success(response.data)
                } else {
                    Result.failure(Exception(response.message))
                }
            }
            is NetworkResult.Error -> Result.failure(Exception(result.message))
            is NetworkResult.Loading -> Result.failure(Exception("Loading"))
        }
    }

    /**
     * 上传头像
     */
    suspend fun uploadAvatar(imageBytes: ByteArray, fileName: String): Result<String> {
        return when (val result = authService.uploadAvatar(imageBytes, fileName)) {
            is NetworkResult.Success -> {
                val response = result.data
                if (response.isSuccess && response.data != null) {
                    Result.success(response.data)
                } else {
                    Result.failure(Exception(response.message))
                }
            }
            is NetworkResult.Error -> Result.failure(Exception(result.message))
            is NetworkResult.Loading -> Result.failure(Exception("Loading"))
        }
    }

    /**
     * 注册
     */
    suspend fun register(request: RegisterRequest): Result<LoginResponse> {
        _loginState.value = LoginState.Loading

        return when (val result = authService.register(request)) {
            is NetworkResult.Success -> {
                val response = result.data
                if (response.isSuccess && response.data != null) {
                    val loginResponse = response.data
                    _currentToken = loginResponse.token
                    _refreshToken = loginResponse.refreshToken
                    _userInfo.value = loginResponse.userInfo
                    _loginState.value = LoginState.LoggedIn(loginResponse)
                    Result.success(loginResponse)
                } else {
                    _loginState.value = LoginState.Error(response.message)
                    Result.failure(Exception(response.message))
                }
            }
            is NetworkResult.Error -> {
                _loginState.value = LoginState.Error(result.message)
                Result.failure(Exception(result.message))
            }
            is NetworkResult.Loading -> Result.failure(Exception("Loading"))
        }
    }

    /**
     * 忘记密码
     */
    suspend fun forgetPassword(request: ForgetPasswordRequest): Result<Unit> {
        return when (val result = authService.forgetPassword(request)) {
            is NetworkResult.Success -> {
                val response = result.data
                if (response.isSuccess) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(response.message))
                }
            }
            is NetworkResult.Error -> Result.failure(Exception(result.message))
            is NetworkResult.Loading -> Result.failure(Exception("Loading"))
        }
    }

    /**
     * 登出
     */
    fun logout() {
        _currentToken = null
        _refreshToken = null
        _userInfo.value = null
        _loginState.value = LoginState.LoggedOut
    }

    /**
     * 获取当前Token
     */
    fun getCurrentToken(): String? = _currentToken

    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Boolean = _currentToken != null

    /**
     * 从本地存储恢复登录状态
     */
    fun restoreLoginState(token: String?, refreshToken: String?, userInfo: UserInfo?) {
        _currentToken = token
        _refreshToken = refreshToken
        _userInfo.value = userInfo
        if (token != null) {
            _loginState.value = LoginState.LoggedIn(
                LoginResponse(
                    token = token,
                    refreshToken = refreshToken ?: "",
                    expiresIn = 0,
                    userInfo = userInfo
                )
            )
        }
    }
}

/**
 * 登录状态
 */
sealed class LoginState {
    data object LoggedOut : LoginState()
    data object Loading : LoginState()
    data class LoggedIn(val loginResponse: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}
