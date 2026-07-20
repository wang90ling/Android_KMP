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

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 使用示例 - 展示网络请求框架的各种用法
 *
 * 此文件展示如何使用封装的 KtorHttpClient 进行各种网络请求
 */

/**
 * 示例1: 基础 GET 请求
 *
 * ```kotlin
 * val client = KtorHttpClient(HttpClient())
 *
 * suspend fun fetchUserList() {
 *     when (val result = client.get<UserList>("/api/users")) {
 *         is NetworkResult.Success -> {
 *             val users = result.data
 *             // 处理用户列表
 *         }
 *         is NetworkResult.Error -> {
 *             // 处理错误
 *             println("Error: ${result.message}")
 *         }
 *         is NetworkResult.Loading -> {
 *             // 显示加载状态
 *         }
 *     }
 * }
 * ```
 */

/**
 * 示例2: POST 请求 (带 JSON Body)
 *
 * ```kotlin
 * data class LoginRequest(val phone: String, val code: String)
 * data class LoginResponse(val token: String, val userId: String)
 *
 * suspend fun login() {
 *     val request = LoginRequest("13800138000", "123456")
 *     when (val result = client.post<LoginResponse, LoginRequest>(
 *         url = "/api/login",
 *         body = request
 *     )) {
 *         is NetworkResult.Success -> {
 *             val response = result.data
 *             saveToken(response.token)
 *         }
 *         is NetworkResult.Error -> {
 *             // 处理错误
 *         }
 *         else -> {}
 *     }
 * }
 * ```
 */

/**
 * 示例3: 文件上传 (图片)
 *
 * ```kotlin
 * suspend fun uploadAvatar(imageBytes: ByteArray) {
 *     val avatarFile = UploadFile.image(
 *         fieldName = "avatar",
 *         fileName = "avatar.jpg",
 *         bytes = imageBytes,
 *         type = UploadFile.ImageType.JPEG
 *     )
 *
 *     // 监听上传进度
 *     launch {
 *         client.uploadProgress.collect { progress ->
 *             when (progress) {
 *                 is UploadProgress.InProgress -> {
 *                     println("Upload: ${progress.percent}%")
 *                 }
 *                 is UploadProgress.Completed -> {
 *                     println("Upload completed!")
 *                 }
 *                 is UploadProgress.Failed -> {
 *                     println("Upload failed: ${progress.error.message}")
 *                 }
 *                 else -> {}
 *             }
 *         }
 *     }
 *
 *     when (val result = client.uploadFile<UploadResponse>(
 *         url = "/api/upload/avatar",
 *         files = listOf(avatarFile),
 *         formFields = mapOf("userId" to "12345")
 *     )) {
 *         is NetworkResult.Success -> {
 *             println("Avatar URL: ${result.data.avatarUrl}")
 *         }
 *         is NetworkResult.Error -> {
 *             // 处理错误
 *         }
 *         else -> {}
 *     }
 * }
 * ```
 */

/**
 * 示例4: 文件上传 (视频)
 *
 * ```kotlin
 * suspend fun uploadVideo(videoBytes: ByteArray) {
 *     val videoFile = UploadFile.video(
 *         fieldName = "video",
 *         fileName = "video.mp4",
 *         bytes = videoBytes,
 *         type = UploadFile.VideoType.MP4
 *     )
 *
 *     // 使用上传专用的超时配置
 *     val uploadClient = KtorHttpClient(
 *         HttpClient {
 *             install(HttpTimeout) {
 *                 requestTimeoutMillis = 300_000 // 5分钟
 *             }
 *         }
 *     )
 *
 *     when (val result = uploadClient.uploadFile<VideoUploadResponse>(
 *         url = "/api/upload/video",
 *         files = listOf(videoFile)
 *     )) {
 *         is NetworkResult.Success -> {
 *             println("Video URL: ${result.data.videoUrl}")
 *         }
 *         is NetworkResult.Error -> {
 *             // 处理错误
 *         }
 *         else -> {}
 *     }
 * }
 * ```
 */

/**
 * 示例5: 混合上传 (图片 + 视频)
 *
 * ```kotlin
 * suspend fun uploadMedia(images: List<ByteArray>, videos: List<ByteArray>) {
 *     val imageFiles = images.mapIndexed { index, bytes ->
 *         UploadFile.image(
 *             fieldName = "images",
 *             fileName = "image_$index.jpg",
 *             bytes = bytes
 *         )
 *     }
 *
 *     val videoFiles = videos.mapIndexed { index, bytes ->
 *         UploadFile.video(
 *             fieldName = "videos",
 *             fileName = "video_$index.mp4",
 *             bytes = bytes
 *         )
 *     }
 *
 *     when (val result = client.uploadMedia<MediaUploadResponse>(
 *         url = "/api/upload/media",
 *         imageFiles = imageFiles,
 *         videoFiles = videoFiles,
 *         formFields = mapOf(
 *             "title" to "My Content",
 *             "description" to "Content description"
 *         )
 *     )) {
 *         is NetworkResult.Success -> {
 *             val response = result.data
 *             println("Image URLs: ${response.imageUrls}")
 *             println("Video URLs: ${response.videoUrls}")
 *         }
 *         is NetworkResult.Error -> {
 *             // 处理错误
 *         }
 *         else -> {}
 *     }
 * }
 * ```
 */

/**
 * 示例6: 带认证的请求
 *
 * ```kotlin
 * suspend fun fetchProtectedData(token: String) {
 *     val headers = mapOf("Authorization" to "Bearer $token")
 *
 *     when (val result = client.get<UserData>(
 *         url = "/api/user/profile",
 *         headers = headers
 *     )) {
 *         is NetworkResult.Success -> {
 *             val userData = result.data
 *             // 处理用户数据
 *         }
 *         is NetworkResult.Error -> {
 *             if (result.code == 401) {
 *                 // Token 过期，需要刷新
 *                 refreshToken()
 *             }
 *         }
 *         else -> {}
 *     }
 * }
 * ```
 */

/**
 * 示例7: 使用 API 路由
 *
 * ```kotlin
 * suspend fun sendSmsCode(phone: String) {
 *     val url = ApiRoutes.getBaseUrl() + ApiRoutes.Auth.SEND_SMS_CODE
 *
 *     val request = SendSmsCodeRequest(phone = phone, type = SmsType.LOGIN.value)
 *
 *     when (val result = client.post<BaseResponse<Unit>, SendSmsCodeRequest>(
 *         url = url,
 *         body = request
 *     )) {
 *         is NetworkResult.Success -> {
 *             if (result.data.isSuccess) {
 *                 // 验证码发送成功
 *             }
 *         }
 *         is NetworkResult.Error -> {
 *             // 处理错误
 *         }
 *         else -> {}
 *     }
 * }
 * ```
 */

/**
 * 示例8: 使用 AuthRepository
 *
 * ```kotlin
 * val authRepository = AuthRepository(authService)
 *
 * // 监听登录状态
 * launch {
 *     authRepository.loginState.collect { state ->
 *         when (state) {
 *             is LoginState.LoggedIn -> {
 *                 // 用户已登录
 *                 val token = state.loginResponse.token
 *                 val userInfo = state.loginResponse.userInfo
 *             }
 *             is LoginState.LoggedOut -> {
 *                 // 用户已登出
 *             }
 *             is LoginState.Loading -> {
 *                 // 登录中
 *             }
 *             is LoginState.Error -> {
 *                 // 登录错误
 *                 println("Error: ${state.message}")
 *             }
 *         }
 *     }
 * }
 *
 * // 执行验证码登录
 * suspend fun performSmsLogin(phone: String, code: String) {
 *     authRepository.smsLogin(phone, code)
 *         .onSuccess { response ->
 *             println("Login success: ${response.token}")
 *         }
 *         .onFailure { error ->
 *             println("Login failed: ${error.message}")
 *         }
 * }
 * ```
 */

/**
 * 示例9: 使用 LoginViewModel
 *
 * ```kotlin
 * val loginViewModel: LoginViewModel = viewModel(
 *     factory = appContainer.loginViewModelFactory
 * )
 *
 * // 观察UI状态
 * LaunchedEffect(Unit) {
 *     loginViewModel.loginUiState.collect { state ->
 *         when (state) {
 *             is LoginUiStateV2.Loading -> {
 *                 // 显示加载
 *             }
 *             is LoginUiStateV2.Success -> {
 *                 // 登录成功，跳转到主页
 *             }
 *             is LoginUiStateV2.Error -> {
 *                 // 显示错误
 *                 showError(state.message)
 *             }
 *             is LoginUiStateV2.Idle -> {
 *                 // 初始状态
 *             }
 *         }
 *     }
 * }
 *
 * // 监听事件
 * LaunchedEffect(Unit) {
 *     loginViewModel.events.collect { event ->
 *         when (event) {
 *             is LoginEvent.SmsCodeSent -> {
 *                 // 验证码已发送
 *             }
 *             is LoginEvent.LoginSuccess -> {
 *                 // 登录成功
 *             }
 *             is LoginEvent.Error -> {
 *                 // 显示错误消息
 *             }
 *             else -> {}
 *         }
 *     }
 * }
 *
 * // 发送验证码
 * fun onSendCode() {
 *     loginViewModel.updatePhone("13800138000")
 *     loginViewModel.sendSmsCode()
 * }
 *
 * // 执行登录
 * fun onLogin() {
 *     loginViewModel.smsLogin()
 * }
 *
 * // 观察倒计时
 * LaunchedEffect(Unit) {
 *     loginViewModel.countdown.collect { seconds ->
 *         if (seconds > 0) {
 *             // 显示倒计时按钮
 *             binding.codeButton.text = "${seconds}s"
 *         } else {
 *             // 重新启用发送按钮
 *             binding.codeButton.text = "获取验证码"
 *         }
 *     }
 * }
 * ```
 */
object UsageExamples
