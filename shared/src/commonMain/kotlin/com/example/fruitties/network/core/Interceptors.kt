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

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlin.math.pow

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 网络请求拦截器 - 提供请求/响应拦截、重试机制、Token 刷新等功能
 */

/**
 * Token 管理器接口
 */
interface TokenManager {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
    suspend fun refreshToken(): String?
}

/**
 * Token 刷新异常 (携带新 Token)
 */
class TokenRefreshException(val newToken: String) : Exception("Token refreshed")

/**
 * 未授权异常
 */
class UnauthorizedException : Exception("Unauthorized access")

/**
 * 重试拦截器
 * 支持指数退避算法的自动重试
 */
class RetryInterceptor(
    private val maxRetries: Int = 3,
    private val baseDelayMillis: Long = 1000,
    private val maxDelayMillis: Long = 10_000
) {

    private val logger = Logger.withTag("RetryInterceptor")

    suspend fun intercept(block: suspend () -> HttpResponse): HttpResponse {
        var lastException: Exception? = null
        for (attempt in 1..maxRetries) {
            try {
                val response = block()
                if (response.status.isSuccess() || !shouldRetry(response.status)) {
                    return response
                }
                val delayMillis = calculateDelay(attempt)
                logger.v { "Retry attempt $attempt/$maxRetries after ${delayMillis}ms" }
                delay(delayMillis)
            } catch (e: Exception) {
                lastException = e
                val delayMillis = calculateDelay(attempt)
                logger.v { "Retry attempt $attempt/$maxRetries after ${delayMillis}ms, error: ${e.message}" }
                delay(delayMillis)
            }
        }
        throw lastException ?: Exception("Max retries exceeded")
    }

    private fun calculateDelay(attempt: Int): Long {
        val exponentialDelay = baseDelayMillis * 2.0.pow(attempt - 1).toLong()
        return minOf(exponentialDelay, maxDelayMillis)
    }

    private fun shouldRetry(status: HttpStatusCode): Boolean {
        return status.value in listOf(
            HttpStatusCode.TooManyRequests.value,
            HttpStatusCode.ServiceUnavailable.value,
            HttpStatusCode.GatewayTimeout.value
        )
    }
}
