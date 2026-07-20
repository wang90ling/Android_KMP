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
package com.example.fruitties.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fruitties.DataRepository
import com.example.fruitties.network.AuthService
import com.example.fruitties.network.AuthServiceImpl
import com.example.fruitties.network.FruittieApi
import com.example.fruitties.network.FruittieNetworkApi
import com.example.fruitties.network.core.ApiRoutes
import com.example.fruitties.network.core.KtorHttpClient
import com.example.fruitties.network.core.TimeoutConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.client.plugins.logging.Logging
import com.example.fruitties.repository.AuthRepository
import com.example.fruitties.viewmodel.CartViewModel
import com.example.fruitties.viewmodel.FruittieViewModel
import com.example.fruitties.viewmodel.FruittieViewModel.Companion.FRUITTIE_ID_KEY
import com.example.fruitties.viewmodel.LoginViewModel
import com.example.fruitties.viewmodel.MainViewModel
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json

val json = Json { ignoreUnknownKeys = true }

class AppContainer(
    private val factory: Factory,
) {
    // ==================== Network ====================

    private val httpClient: HttpClient by lazy {
        HttpClient(factory.createHttpEngine()) {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Any)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = TimeoutConfig.DEFAULT.requestTimeoutMillis
                connectTimeoutMillis = TimeoutConfig.DEFAULT.connectTimeoutMillis
                socketTimeoutMillis = TimeoutConfig.DEFAULT.socketTimeoutMillis
            }
            install(Logging) {
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        Logger.d { "HTTP: $message" }
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    private val ktorHttpClient: KtorHttpClient by lazy {
        KtorHttpClient(httpClient)
    }

    private val authService: AuthService by lazy {
        AuthServiceImpl(
            httpClient = ktorHttpClient,
            baseUrl = ApiRoutes.getBaseUrl(),
            json = json
        )
    }

    // ==================== Repository ====================

    val authRepository: AuthRepository by lazy {
        AuthRepository(authService = authService)
    }

    val dataRepository: DataRepository by lazy {
        DataRepository(
            api = commonCreateApi(),
            database = factory.createRoomDatabase(),
            cartDataStore = factory.createCartDataStore(),
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
        )
    }

    // ==================== ViewModels ====================

    val mainViewModelFactory = viewModelFactory {
        initializer {
            MainViewModel(repository = dataRepository)
        }
    }

    val cartViewModelFactory = viewModelFactory {
        initializer {
            CartViewModel(repository = dataRepository)
        }
    }

    val fruittieViewModelFactory = viewModelFactory {
        initializer {
            FruittieViewModel(
                fruittieId = this[FRUITTIE_ID_KEY] ?: error("Expected fruittieId!"),
                repository = dataRepository,
            )
        }
    }

    val loginViewModelFactory = viewModelFactory {
        initializer {
            LoginViewModel(
                repository = dataRepository,
                authRepository = authRepository
            )
        }
    }

    internal fun commonCreateApi(): FruittieApi =
        FruittieNetworkApi(
            client = httpClient,
            apiUrl = "https://android.github.io/kotlin-multiplatform-samples/fruitties-api",
        )
}
