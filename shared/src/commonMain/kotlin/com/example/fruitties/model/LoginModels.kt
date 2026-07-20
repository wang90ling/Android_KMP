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
package com.example.fruitties.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 登录相关的数据模型
 */

/**
 * 发送验证码请求
 */
@Serializable
data class SendSmsCodeRequest(
    @SerialName("phone")
    val phone: String,
    @SerialName("type")
    val type: Int = SmsType.LOGIN.value  // 默认登录类型
)

/**
 * 验证码类型
 */
enum class SmsType(val value: Int) {
    LOGIN(1),           // 登录
    REGISTER(2),        // 注册
    RESET_PASSWORD(3),  // 重置密码
    BIND_PHONE(4)       // 绑定手机
}

/**
 * 验证验证码请求
 */
@Serializable
data class VerifySmsCodeRequest(
    @SerialName("phone")
    val phone: String,
    @SerialName("code")
    val code: String
)

/**
 * 验证码登录请求
 */
@Serializable
data class SmsLoginRequest(
    @SerialName("phone")
    val phone: String,
    @SerialName("code")
    val code: String,
    @SerialName("deviceId")
    val deviceId: String? = null,
    @SerialName("deviceType")
    val deviceType: Int = DeviceType.ANDROID.value
)

/**
 * 设备类型
 */
enum class DeviceType(val value: Int) {
    ANDROID(1),
    IOS(2),
    WEB(3)
}

/**
 * 登录响应
 */
@Serializable
data class LoginResponse(
    @SerialName("token")
    val token: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("expiresIn")
    val expiresIn: Long,
    @SerialName("userInfo")
    val userInfo: UserInfo? = null
)

/**
 * 用户信息
 */
@Serializable
data class UserInfo(
    @SerialName("userId")
    val userId: String,
    @SerialName("username")
    val username: String? = null,
    @SerialName("nickname")
    val nickname: String? = null,
    @SerialName("avatar")
    val avatar: String? = null,
    @SerialName("phone")
    val phone: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("gender")
    val gender: Int = 0,
    @SerialName("createTime")
    val createTime: Long = 0
)

/**
 * 刷新 Token 请求
 */
@Serializable
data class RefreshTokenRequest(
    @SerialName("refreshToken")
    val refreshToken: String
)

/**
 * 刷新 Token 响应
 */
@Serializable
data class RefreshTokenResponse(
    @SerialName("token")
    val token: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("expiresIn")
    val expiresIn: Long
)

/**
 * 注册请求
 */
@Serializable
data class RegisterRequest(
    @SerialName("phone")
    val phone: String,
    @SerialName("code")
    val code: String,
    @SerialName("password")
    val password: String,
    @SerialName("nickname")
    val nickname: String? = null
)

/**
 * 忘记密码请求
 */
@Serializable
data class ForgetPasswordRequest(
    @SerialName("phone")
    val phone: String,
    @SerialName("code")
    val code: String,
    @SerialName("newPassword")
    val newPassword: String
)
