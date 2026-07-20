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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 统一网络请求结果封装
 */
@Serializable
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(
        val code: Int = -1,
        val message: String,
        val throwable: Throwable? = null
    ) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description API基础响应体
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
}

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 分页响应体
 */
@Serializable
data class PageResponse<T>(
    @SerialName("page")
    val page: Int,
    @SerialName("pageSize")
    val pageSize: Int,
    @SerialName("total")
    val total: Long,
    @SerialName("list")
    val list: List<T>
)
