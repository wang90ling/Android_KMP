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

package com.example.fruitties.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.fruitties.android.ui.AppTheme
import com.example.fruitties.android.ui.circle.CircleScreen
import com.example.fruitties.android.ui.im.MessageScreen
import com.example.fruitties.android.ui.login.LoginScreen
import com.example.fruitties.android.ui.main.MainScreen
import com.example.fruitties.android.ui.mine.MineScreen
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenKey : NavKey

@Serializable
data object LoginScreenKey : NavKey

@Serializable
data object CircleScreenKey : NavKey

@Serializable
data object MessageScreenKey : NavKey

@Serializable
data object MineScreenKey : NavKey

@Serializable
data object ListScreenKey : NavKey

@Serializable
data object CartScreenKey : NavKey

@Serializable
data class FruittieScreenKey(
    val id: Long,
) : NavKey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalAppContainer provides (this.applicationContext as FruittiesAndroidApp).container,
            ) {
                AppTheme() {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        NavApp()
                    }
                }
            }
        }
    }
}

@Composable
fun NavApp() {
    val backStack = rememberNavBackStack(MainScreenKey)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<MainScreenKey> {
                MainScreen(
                    onCartClick = {
                        backStack.add(CartScreenKey)
                    },
                    onProductClick = { productName ->
                        // Navigate to detail if needed
                    },
                )
            }
            entry<LoginScreenKey> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.removeIf { it is LoginScreenKey }
                    },
                    onRegisterClick = {
                        // Navigate to register screen if needed
                    },
                )
            }
            entry<CircleScreenKey> {
                CircleScreen(
                    onPostClick = { username ->
                        // Navigate to post detail if needed
                    },
                )
            }
            entry<MessageScreenKey> {
                MessageScreen { content ->
                    // Navigate to notification detail if needed
                }
            }
            entry<MineScreenKey> {
                MineScreen { menuTitle ->
                    // Handle menu item click
                }
            }
        },
    )
}
