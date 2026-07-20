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
 * @description 通用基础响应模型
 */

/**
 * 基础API响应体
 */
@Serializable
data class BaseResponse<T>(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T? = null
) {
    val isSuccess: Boolean
        get() = code == 200 || code == 0

    companion object {
        fun <T> success(data: T, message: String = "Success"): BaseResponse<T> =
            BaseResponse(code = 200, message = message, data = data)

        fun <T> error(code: Int, message: String): BaseResponse<T> =
            BaseResponse(code = code, message = message, data = null)
    }
}

/**
 * 空响应
 */
@Serializable
data class EmptyResponse(
    @SerialName("success")
    val success: Boolean = true
)
