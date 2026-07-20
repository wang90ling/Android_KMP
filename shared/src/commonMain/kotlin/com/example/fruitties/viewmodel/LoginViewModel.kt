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
package com.example.fruitties.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.fruitties.DataRepository
import com.example.fruitties.model.CartItemDetails
import com.example.fruitties.model.LoginResponse
import com.example.fruitties.model.SmsType
import com.example.fruitties.model.UserInfo
import com.example.fruitties.network.core.UploadProgress
import com.example.fruitties.repository.AuthRepository
import com.example.fruitties.repository.LoginState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 登录 ViewModel - 处理验证码登录相关逻辑
 *
 * 功能包括:
 * - 发送验证码
 * - 验证码倒计时
 * - 验证码登录
 * - 用户信息管理
 * - 头像上传
 * - 购物车相关 (继承自原有逻辑)
 */
class LoginViewModel(
    private val repository: DataRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    init {
        Logger.v { "LoginViewModel created" }
        observeLoginState()
    }

    override fun onCleared() {
        super.onCleared()
        Logger.v { "LoginViewModel cleared" }
    }

    // ==================== 原有购物车相关逻辑 ====================

    val cartUiState: StateFlow<CartUiState> =
        repository.cartDetails
            .map { details ->
                CartUiState(cartDetails = details)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CartUiState(),
            )

    fun increaseCountClick(cartItem: CartItemDetails) {
        viewModelScope.launch {
            repository.addToCart(cartItem.fruittie)
        }
    }

    fun decreaseCountClick(cartItem: CartItemDetails) {
        viewModelScope.launch {
            repository.removeFromCart(cartItem.fruittie)
        }
    }

    // ==================== 验证码登录相关逻辑 ====================

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _smsCode = MutableStateFlow("")
    val smsCode: StateFlow<String> = _smsCode.asStateFlow()

    private val _countdown = MutableStateFlow(0)
    val countdown: StateFlow<Int> = _countdown.asStateFlow()

    private val _loginUiState = MutableStateFlow<LoginUiStateV2>(LoginUiStateV2.Idle)
    val loginUiState: StateFlow<LoginUiStateV2> = _loginUiState.asStateFlow()

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    private val _uploadProgress = MutableStateFlow<UploadProgress>(UploadProgress.Idle)
    val uploadProgress: StateFlow<UploadProgress> = _uploadProgress.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    /**
     * 监听登录状态变化
     */
    private fun observeLoginState() {
        viewModelScope.launch {
            authRepository.loginState.collect { state ->
                when (state) {
                    is LoginState.LoggedOut -> {
                        _loginUiState.value = LoginUiStateV2.Idle
                    }
                    is LoginState.Loading -> {
                        _loginUiState.value = LoginUiStateV2.Loading
                    }
                    is LoginState.LoggedIn -> {
                        _loginUiState.value = LoginUiStateV2.Success(state.loginResponse)
                        _userInfo.value = state.loginResponse.userInfo
                        _events.emit(LoginEvent.LoginSuccess(state.loginResponse))
                    }
                    is LoginState.Error -> {
                        _loginUiState.value = LoginUiStateV2.Error(state.message)
                        _events.emit(LoginEvent.LoginError(state.message))
                    }
                }
            }
        }
    }

    /**
     * 更新手机号
     */
    fun updatePhone(phone: String) {
        _phone.value = phone
    }

    /**
     * 更新验证码
     */
    fun updateSmsCode(code: String) {
        _smsCode.value = code
    }

    /**
     * 发送验证码
     */
    fun sendSmsCode(smsType: SmsType = SmsType.LOGIN) {
        val currentPhone = _phone.value
        if (!validatePhone(currentPhone)) {
            viewModelScope.launch {
                _events.emit(LoginEvent.Error("请输入正确的手机号"))
            }
            return
        }

        viewModelScope.launch {
            _loginUiState.value = LoginUiStateV2.Loading

            authRepository.sendSmsCode(currentPhone, smsType)
                .onSuccess {
                    startCountdown()
                    _events.emit(LoginEvent.SmsCodeSent)
                }
                .onFailure { error ->
                    _loginUiState.value = LoginUiStateV2.Error(error.message ?: "发送失败")
                    _events.emit(LoginEvent.Error(error.message ?: "发送验证码失败"))
                }
        }
    }

    /**
     * 验证码登录
     */
    fun smsLogin(deviceId: String? = null, deviceType: Int = 1) {
        val currentPhone = _phone.value
        val currentCode = _smsCode.value

        if (!validatePhone(currentPhone)) {
            viewModelScope.launch {
                _events.emit(LoginEvent.Error("请输入正确的手机号"))
            }
            return
        }

        if (!validateSmsCode(currentCode)) {
            viewModelScope.launch {
                _events.emit(LoginEvent.Error("请输入6位验证码"))
            }
            return
        }

        viewModelScope.launch {
            _loginUiState.value = LoginUiStateV2.Loading

            authRepository.smsLogin(
                phone = currentPhone,
                code = currentCode,
                deviceId = deviceId,
                deviceType = deviceType
            ).onSuccess { response ->
                    Logger.v { "Login successful: ${response.token}" }
                }
                .onFailure { error ->
                    _events.emit(LoginEvent.Error(error.message ?: "登录失败"))
                }
        }
    }

    /**
     * 登出
     */
    fun logout() {
        authRepository.logout()
        clearInput()
    }

    /**
     * 清除输入
     */
    fun clearInput() {
        _phone.value = ""
        _smsCode.value = ""
    }

    /**
     * 获取用户信息
     */
    fun fetchUserProfile() {
        viewModelScope.launch {
            authRepository.fetchUserProfile()
                .onSuccess { userInfo ->
                    _userInfo.value = userInfo
                }
                .onFailure { error ->
                    _events.emit(LoginEvent.Error(error.message ?: "获取用户信息失败"))
                }
        }
    }

    /**
     * 更新用户信息
     */
    fun updateProfile(
        nickname: String? = null,
        avatar: String? = null,
        gender: Int? = null,
        email: String? = null
    ) {
        viewModelScope.launch {
            authRepository.updateProfile(nickname, avatar, gender, email)
                .onSuccess { userInfo ->
                    _userInfo.value = userInfo
                    _events.emit(LoginEvent.ProfileUpdated(userInfo))
                }
                .onFailure { error ->
                    _events.emit(LoginEvent.Error(error.message ?: "更新用户信息失败"))
                }
        }
    }

    /**
     * 上传头像
     */
    fun uploadAvatar(imageBytes: ByteArray, fileName: String) {
        viewModelScope.launch {
            _uploadProgress.value = UploadProgress.Started

            authRepository.uploadAvatar(imageBytes, fileName)
                .onSuccess { avatarUrl ->
                    _uploadProgress.value = UploadProgress.Completed
                    _userInfo.value = _userInfo.value?.copy(avatar = avatarUrl)
                    _events.emit(LoginEvent.AvatarUploaded(avatarUrl))
                }
                .onFailure { error ->
                    _uploadProgress.value = UploadProgress.Failed(Exception(error.message))
                    _events.emit(LoginEvent.Error(error.message ?: "上传头像失败"))
                }
        }
    }

    /**
     * 刷新Token
     */
    fun refreshToken() {
        viewModelScope.launch {
            authRepository.refreshToken()
                .onSuccess { response ->
                    Logger.v { "Token refreshed successfully" }
                }
                .onFailure { error ->
                    _events.emit(LoginEvent.Error(error.message ?: "Token刷新失败"))
                }
        }
    }

    /**
     * 验证手机号
     */
    private fun validatePhone(phone: String): Boolean {
        return phone.length == 11 && phone.all { it.isDigit() }
    }

    /**
     * 验证验证码
     */
    private fun validateSmsCode(code: String): Boolean {
        return code.length == 6 && code.all { it.isDigit() }
    }

    /**
     * 启动倒计时
     */
    private fun startCountdown() {
        viewModelScope.launch {
            _countdown.value = COUNTDOWN_SECONDS
            while (_countdown.value > 0) {
                kotlinx.coroutines.delay(1000)
                _countdown.value = _countdown.value - 1
            }
        }
    }

    /**
     * 是否可以发送验证码
     */
    fun canSendCode(): Boolean = _countdown.value == 0

    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        private const val COUNTDOWN_SECONDS = 60
    }
}

/**
 * 登录UI状态
 */
sealed class LoginUiStateV2 {
    data object Idle : LoginUiStateV2()
    data object Loading : LoginUiStateV2()
    data class Success(val loginResponse: LoginResponse) : LoginUiStateV2()
    data class Error(val message: String) : LoginUiStateV2()
}

/**
 * 登录事件
 */
sealed class LoginEvent {
    data object SmsCodeSent : LoginEvent()
    data class LoginSuccess(val loginResponse: LoginResponse) : LoginEvent()
    data class LoginError(val message: String) : LoginEvent()
    data class ProfileUpdated(val userInfo: UserInfo) : LoginEvent()
    data class AvatarUploaded(val avatarUrl: String) : LoginEvent()
    data class Error(val message: String) : LoginEvent()
}

/**
 * 购物车UI状态 (保留原有定义)
 */
data class LoginUiState(
    val cartDetails: List<CartItemDetails> = listOf(),
) {
    val totalItemCount: Int = cartDetails.sumOf { it.count }
}
