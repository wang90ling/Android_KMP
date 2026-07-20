/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, this
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.fruitties.network

import com.example.fruitties.model.BaseResponse
import com.example.fruitties.model.ForgetPasswordRequest
import com.example.fruitties.model.LoginResponse
import com.example.fruitties.model.RefreshTokenResponse
import com.example.fruitties.model.RegisterRequest
import com.example.fruitties.model.SendSmsCodeRequest
import com.example.fruitties.model.SmsLoginRequest
import com.example.fruitties.model.UserInfo
import com.example.fruitties.model.VerifySmsCodeRequest
import com.example.fruitties.network.core.ApiRoutes
import com.example.fruitties.network.core.KtorHttpClient
import com.example.fruitties.network.core.NetworkResult
import com.example.fruitties.network.core.UploadFile
import kotlinx.serialization.json.Json

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 登录服务接口 - 验证码登录相关功能
 *
 * 功能包括:
 * - 发送验证码
 * - 验证验证码
 * - 验证码登录
 * - Token 刷新
 * - 用户信息获取/更新
 * - 头像上传
 */
interface AuthService {

    /**
     * 发送验证码
     * @param phone 手机号
     * @param type 验证码类型
     */
    suspend fun sendSmsCode(phone: String, type: Int = 1): NetworkResult<BaseResponse<Unit>>

    /**
     * 验证验证码
     * @param phone 手机号
     * @param code 验证码
     */
    suspend fun verifySmsCode(phone: String, code: String): NetworkResult<BaseResponse<Unit>>

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
    ): NetworkResult<BaseResponse<LoginResponse>>

    /**
     * 刷新Token
     * @param refreshToken 刷新令牌
     */
    suspend fun refreshToken(refreshToken: String): NetworkResult<BaseResponse<RefreshTokenResponse>>

    /**
     * 获取用户信息
     */
    suspend fun getUserProfile(): NetworkResult<BaseResponse<UserInfo>>

    /**
     * 更新用户信息
     * @param nickname 昵称
     * @param avatar 头像URL
     * @param gender 性别
     * @param email 邮箱
     */
    suspend fun updateProfile(
        nickname: String? = null,
        avatar: String? = null,
        gender: Int? = null,
        email: String? = null
    ): NetworkResult<BaseResponse<UserInfo>>

    /**
     * 上传头像
     * @param imageBytes 头像图片字节数组
     * @param fileName 文件名
     */
    suspend fun uploadAvatar(
        imageBytes: ByteArray,
        fileName: String
    ): NetworkResult<BaseResponse<String>>

    /**
     * 注册
     * @param request 注册信息
     */
    suspend fun register(request: RegisterRequest): NetworkResult<BaseResponse<LoginResponse>>

    /**
     * 忘记密码
     * @param request 忘记密码请求
     */
    suspend fun forgetPassword(request: ForgetPasswordRequest): NetworkResult<BaseResponse<Unit>>
}

/**
 * 登录服务实现
 */
class AuthServiceImpl(
    private val httpClient: KtorHttpClient,
    private val baseUrl: String = ApiRoutes.getBaseUrl(),
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
) : AuthService {

    override suspend fun sendSmsCode(phone: String, type: Int): NetworkResult<BaseResponse<Unit>> {
        val request = SendSmsCodeRequest(phone = phone, type = type)
        return httpClient.post(
            url = "$baseUrl${ApiRoutes.Auth.SEND_SMS_CODE}",
            body = request,
            jsonSerializer = json
        )
    }

    override suspend fun verifySmsCode(phone: String, code: String): NetworkResult<BaseResponse<Unit>> {
        val request = VerifySmsCodeRequest(phone = phone, code = code)
        return httpClient.post(
            url = "$baseUrl${ApiRoutes.Auth.VERIFY_SMS_CODE}",
            body = request,
            jsonSerializer = json
        )
    }

    override suspend fun smsLogin(
        phone: String,
        code: String,
        deviceId: String?,
        deviceType: Int
    ): NetworkResult<BaseResponse<LoginResponse>> {
        val request = SmsLoginRequest(
            phone = phone,
            code = code,
            deviceId = deviceId,
            deviceType = deviceType
        )
        return httpClient.post(
            url = "$baseUrl${ApiRoutes.Auth.LOGIN_BY_SMS}",
            body = request,
            jsonSerializer = json
        )
    }

    override suspend fun refreshToken(refreshToken: String): NetworkResult<BaseResponse<RefreshTokenResponse>> {
        return httpClient.post(
            url = "$baseUrl${ApiRoutes.Auth.REFRESH_TOKEN}",
            body = mapOf("refreshToken" to refreshToken),
            jsonSerializer = json
        )
    }

    override suspend fun getUserProfile(): NetworkResult<BaseResponse<UserInfo>> {
        return httpClient.get(
            url = "$baseUrl${ApiRoutes.User.PROFILE}",
            jsonSerializer = json
        )
    }

    override suspend fun updateProfile(
        nickname: String?,
        avatar: String?,
        gender: Int?,
        email: String?
    ): NetworkResult<BaseResponse<UserInfo>> {
        val body = buildMap {
            nickname?.let { put("nickname", it) }
            avatar?.let { put("avatar", it) }
            gender?.let { put("gender", it) }
            email?.let { put("email", it) }
        }
        return httpClient.post(
            url = "$baseUrl${ApiRoutes.User.UPDATE_PROFILE}",
            body = body,
            jsonSerializer = json
        )
    }

    override suspend fun uploadAvatar(
        imageBytes: ByteArray,
        fileName: String
    ): NetworkResult<BaseResponse<String>> {
        val avatarFile = UploadFile.image(
            fieldName = "avatar",
            fileName = fileName,
            bytes = imageBytes,
            type = UploadFile.ImageType.JPEG
        )
        return httpClient.uploadFile(
            url = "$baseUrl${ApiRoutes.User.UPLOAD_AVATAR}",
            files = listOf(avatarFile),
            jsonSerializer = json
        )
    }

    override suspend fun register(request: RegisterRequest): NetworkResult<BaseResponse<LoginResponse>> {
        return httpClient.post(
            url = "$baseUrl${ApiRoutes.Auth.REGISTER}",
            body = request,
            jsonSerializer = json
        )
    }

    override suspend fun forgetPassword(request: ForgetPasswordRequest): NetworkResult<BaseResponse<Unit>> {
        return httpClient.post(
            url = "$baseUrl${ApiRoutes.Auth.FORGET_PASSWORD}",
            body = request,
            jsonSerializer = json
        )
    }
}
