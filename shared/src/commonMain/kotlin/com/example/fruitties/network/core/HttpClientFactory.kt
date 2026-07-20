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
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description HttpClient 工厂类 - 创建跨平台 HttpClient 实例
 *
 * 此类封装了 HttpClient 的创建过程，支持配置:
 * - 超时设置 (连接超时、读取超时、写入超时)
 * - 日志配置
 * - 请求/响应拦截
 * - SSL 证书验证 (在非生产环境禁用)
 */
object HttpClientFactory {

    private const val TAG = "HttpClientFactory"

    /**
     * 创建默认配置的 HttpClient
     * @param engine 平台特定的 HttpClientEngine (可选)
     * @param baseUrl 基础 URL (可选)
     * @param timeoutConfig 超时配置 (可选)
     */
    fun create(
        engine: HttpClientEngine? = null,
        baseUrl: String? = null,
        timeoutConfig: TimeoutConfig = TimeoutConfig()
    ): HttpClient {
        return if (engine != null) {
            HttpClient(engine) {
                configureDefaultRequest(baseUrl)
                configureTimeout(timeoutConfig)
                configureLogging()
            }
        } else {
            HttpClient {
                configureDefaultRequest(baseUrl)
                configureTimeout(timeoutConfig)
                configureLogging()
            }
        }
    }

    /**
     * 创建带有自定义配置的 HttpClient
     * @param engine 平台特定的 HttpClientEngine
     * @param json Json 配置
     * @param timeoutConfig 超时配置
     * @param baseUrl 基础 URL
     * @param authToken 认证 Token (可选)
     */
    fun createWithConfig(
        engine: HttpClientEngine,
        json: Json = KtorHttpClient.defaultJson,
        timeoutConfig: TimeoutConfig = TimeoutConfig(),
        baseUrl: String? = null,
        authToken: String? = null
    ): HttpClient {
        return HttpClient(engine) {
            configureDefaultRequest(baseUrl, authToken)
            configureTimeout(timeoutConfig)
            configureLogging()
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Any)
            }
        }
    }

    /**
     * 创建用于文件上传的 HttpClient
     * @param engine 平台特定的 HttpClientEngine
     * @param timeoutConfig 超时配置 (上传需要更大的超时时间)
     */
    fun createForUpload(
        engine: HttpClientEngine,
        timeoutConfig: TimeoutConfig = TimeoutConfig(
            connectTimeoutMillis = 30_000,
            requestTimeoutMillis = 300_000,
            socketTimeoutMillis = 300_000
        )
    ): HttpClient {
        return HttpClient(engine) {
            configureTimeout(timeoutConfig)
            configureLogging()
        }
    }

    private fun io.ktor.client.HttpClientConfig<*>.configureDefaultRequest(baseUrl: String?, authToken: String? = null) {
        defaultRequest {
            baseUrl?.let { url ->
                url(url)
            }
            header(HttpHeaders.Accept, ContentType.Application.Json)
            authToken?.let { token ->
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    private fun io.ktor.client.HttpClientConfig<*>.configureTimeout(config: TimeoutConfig) {
        install(HttpTimeout) {
            connectTimeoutMillis = config.connectTimeoutMillis
            requestTimeoutMillis = config.requestTimeoutMillis
            socketTimeoutMillis = config.socketTimeoutMillis
        }
    }

    private fun io.ktor.client.HttpClientConfig<*>.configureLogging() {
        install(Logging) {
            logger = object : KtorLogger {
                override fun log(message: String) {
                    Logger.v(TAG) { message }
                }
            }
            level = LogLevel.BODY
        }
    }
}

/**
 * 超时配置
 */
data class TimeoutConfig(
    val connectTimeoutMillis: Long = 15_000,
    val requestTimeoutMillis: Long = 30_000,
    val socketTimeoutMillis: Long = 30_000
) {
    companion object {
        val DEFAULT = TimeoutConfig()
        val UPLOAD = TimeoutConfig(
            connectTimeoutMillis = 30_000,
            requestTimeoutMillis = 300_000,
            socketTimeoutMillis = 300_000
        )
        val LARGE_UPLOAD = TimeoutConfig(
            connectTimeoutMillis = 60_000,
            requestTimeoutMillis = 600_000,
            socketTimeoutMillis = 600_000
        )
    }
}
