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
 * @description API 路由定义 - 统一管理所有 API 接口地址
 */
object ApiRoutes {

    // 开发环境 API 地址
    const val DEV_BASE_URL = "https://dev-api.example.com"

    // 测试环境 API 地址
    const val STAGING_BASE_URL = "https://staging-api.example.com"

    // 生产环境 API 地址
    const val PROD_BASE_URL = "https://api.example.com"

    /**
     * 获取当前环境的 API 地址
     */
    fun getBaseUrl(environment: Environment = Environment.PROD): String {
        return when (environment) {
            Environment.DEV -> DEV_BASE_URL
            Environment.STAGING -> STAGING_BASE_URL
            Environment.PROD -> PROD_BASE_URL
        }
    }

    /**
     * 登录模块 API
     */
    object Auth {
        private const val PREFIX = "/auth"

        const val SEND_SMS_CODE = "$PREFIX/sms/send"           // 发送验证码
        const val VERIFY_SMS_CODE = "$PREFIX/sms/verify"       // 验证验证码
        const val LOGIN = "$PREFIX/login"                       // 登录
        const val LOGIN_BY_SMS = "$PREFIX/login/sms"            // 验证码登录
        const val LOGOUT = "$PREFIX/logout"                    // 登出
        const val REFRESH_TOKEN = "$PREFIX/refresh"            // 刷新Token
        const val REGISTER = "$PREFIX/register"                 // 注册
        const val FORGET_PASSWORD = "$PREFIX/password/forget" // 忘记密码
    }

    /**
     * 用户模块 API
     */
    object User {
        private const val PREFIX = "/user"

        const val PROFILE = "$PREFIX/profile"           // 获取用户信息
        const val UPDATE_PROFILE = "$PREFIX/profile"    // 更新用户信息
        const val UPLOAD_AVATAR = "$PREFIX/avatar"      // 上传头像
    }

    /**
     * 媒体模块 API
     */
    object Media {
        private const val PREFIX = "/media"

        const val UPLOAD_IMAGE = "$PREFIX/upload/image"  // 上传图片
        const val UPLOAD_VIDEO = "$PREFIX/upload/video" // 上传视频
        const val UPLOAD_MEDIA = "$PREFIX/upload/media" // 上传图片/视频
    }

    /**
     * 环境枚举
     */
    enum class Environment {
        DEV,
        STAGING,
        PROD
    }
}
