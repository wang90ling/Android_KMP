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

import io.ktor.client.engine.HttpClientEngine

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 平台特定的 HttpClient 引擎接口
 *
 * 不同的目标平台需要提供不同的 HTTP 引擎实现:
 * - Android: OkHttp (已在 build.gradle.kts 中配置)
 * - iOS: Darwin (已在 build.gradle.kts 中配置)
 * - Web: Js (通过 ktor-client-js)
 * - JVM: CIO (通过 ktor-client-cio)
 */

/**
 * 获取当前平台的 HttpClientEngine
 * 在不同平台的源代码集中实现
 */
expect fun getHttpClientEngine(): HttpClientEngine

/**
 * 获取当前平台的名称
 */
expect fun getPlatformName(): String

/**
 * 平台特定配置
 */
object PlatformConfig {
    val ENGINE: HttpClientEngine by lazy { getHttpClientEngine() }
    val PLATFORM_NAME: String by lazy { getPlatformName() }
}
