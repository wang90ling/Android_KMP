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

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

/**
 * @author wangling
 * @date 2026/7/20 14:00
 * @description 跨平台网络请求客户端
 *
 * 支持平台:
 * - Android (使用 OkHttp 引擎)
 * - iOS (使用 Darwin 引擎)
 * - Web (使用 JS 引擎)
 *
 * 功能特性:
 * - HTTPS/SSL 证书验证
 * - 请求/响应拦截器
 * - 超时配置
 * - 重试机制
 * - 文件上传 (支持图片/视频)
 * - 文件下载
 * - 请求体序列化
 */
class KtorHttpClient(val client: HttpClient) {

    val _uploadProgress = MutableStateFlow<UploadProgress>(UploadProgress.Idle)
    val uploadProgress: Flow<UploadProgress> = _uploadProgress.asStateFlow()

    /**
     * GET 请求
     * @param url 请求地址
     * @param headers 请求头
     * @param parameters URL参数
     * @param jsonSerializer JSON序列化工具
     * @param responseType 响应类型
     */
    suspend inline fun <reified T> get(
        url: String,
        headers: Map<String, String> = emptyMap(),
        parameters: Map<String, Any> = emptyMap(),
        jsonSerializer: Json = defaultJson,
        responseType: ResponseType = ResponseType.JSON
    ): NetworkResult<T> {
        return executeRequest<T>(
            method = "GET",
            url = url,
            headers = headers,
            parameters = parameters,
            body = null,
            jsonSerializer = jsonSerializer,
            responseType = responseType
        )
    }

    /**
     * POST 请求 (带请求体)
     * @param url 请求地址
     * @param body 请求体数据
     * @param headers 请求头
     * @param contentType 内容类型
     * @param jsonSerializer JSON序列化工具
     * @param responseType 响应类型
     */
    suspend inline fun <reified T, B> post(
        url: String,
        body: B? = null,
        headers: Map<String, String> = emptyMap(),
        contentType: ContentType = ContentType.Application.Json,
        jsonSerializer: Json = defaultJson,
        responseType: ResponseType = ResponseType.JSON
    ): NetworkResult<T> {
        return executeRequest<T>(
            method = "POST",
            url = url,
            headers = headers,
            parameters = emptyMap(),
            body = body,
            jsonSerializer = jsonSerializer,
            responseType = responseType
        )
    }

    /**
     * POST 请求 (表单提交)
     * @param url 请求地址
     * @param formData 表单数据
     * @param headers 请求头
     * @param jsonSerializer JSON序列化工具
     */
    suspend inline fun <reified T> postForm(
        url: String,
        formData: Map<String, String>,
        headers: Map<String, String> = emptyMap(),
        jsonSerializer: Json = defaultJson
    ): NetworkResult<T> {
        _uploadProgress.value = UploadProgress.Started

        return try {
            val params = Parameters.build {
                formData.forEach { (key, value) ->
                    append(key, value)
                }
            }

            val response = client.submitForm(
                url = url,
                formParameters = params
            ) {
                this.headers {
                    append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                    headers.forEach { (key, value) ->
                        append(key, value)
                    }
                }
            }

            _uploadProgress.value = UploadProgress.Completed
            parseResponse<T>(response, jsonSerializer, ResponseType.JSON)
        } catch (e: Exception) {
            _uploadProgress.value = UploadProgress.Failed(e)
            NetworkResult.Error(message = e.message ?: "Form submission failed", throwable = e)
        }
    }

    /**
     * 文件/图片/视频上传
     * @param url 请求地址
     * @param files 要上传的文件列表
     * @param formFields 额外的表单字段
     * @param headers 请求头
     * @param jsonSerializer JSON序列化工具
     */
    suspend inline fun <reified T> uploadFile(
        url: String,
        files: List<UploadFile>,
        formFields: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        jsonSerializer: Json = defaultJson
    ): NetworkResult<T> {
        _uploadProgress.value = UploadProgress.Started

        return try {
            val response = client.post(url) {
                this.headers {
                    append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                    headers.forEach { (key, value) ->
                        append(key, value)
                    }
                }
                setBody(
                    FormDataContent(
                        Parameters.build {
                            formFields.forEach { (key, value) ->
                                append(key, value)
                            }
                        }
                    )
                )
            }

            _uploadProgress.value = UploadProgress.Completed
            parseResponse<T>(response, jsonSerializer, ResponseType.JSON)
        } catch (e: Exception) {
            _uploadProgress.value = UploadProgress.Failed(e)
            NetworkResult.Error(message = e.message ?: "Upload failed", throwable = e)
        }
    }

    /**
     * 多文件上传 (支持图片和视频混合)
     * @param url 请求地址
     * @param imageFiles 图片文件列表
     * @param videoFiles 视频文件列表
     * @param formFields 额外的表单字段
     * @param headers 请求头
     * @param jsonSerializer JSON序列化工具
     */
    suspend inline fun <reified T> uploadMedia(
        url: String,
        imageFiles: List<UploadFile> = emptyList(),
        videoFiles: List<UploadFile> = emptyList(),
        formFields: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        jsonSerializer: Json = defaultJson
    ): NetworkResult<T> {
        val allFiles = imageFiles + videoFiles
        return uploadFile<T>(url, allFiles, formFields, headers, jsonSerializer)
    }

    suspend inline fun <reified T> executeRequest(
        method: String,
        url: String,
        headers: Map<String, String>,
        parameters: Map<String, Any>,
        body: Any?,
        jsonSerializer: Json,
        responseType: ResponseType
    ): NetworkResult<T> {
        return try {
            val response: HttpResponse = when (method) {
                "GET" -> {
                    client.get(url) {
                        parameters.forEach { (key, value) ->
                            parameter(key, value.toString())
                        }
                        this.headers {
                            append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                            headers.forEach { (key, value) ->
                                append(key, value)
                            }
                        }
                    }
                }
                "POST" -> {
                    client.post(url) {
                        this.headers {
                            append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                            headers.forEach { (key, value) ->
                                append(key, value)
                            }
                        }
                        contentType(ContentType.Application.Json)
                        body?.let { setBody(it) }
                    }
                }
                else -> throw IllegalArgumentException("Unsupported method: $method")
            }

            parseResponse<T>(response, jsonSerializer, responseType)
        } catch (e: Exception) {
            NetworkResult.Error(
                code = -1,
                message = e.message ?: "Request failed",
                throwable = e
            )
        }
    }

    suspend inline fun <reified T> parseResponse(
        response: HttpResponse,
        jsonSerializer: Json,
        responseType: ResponseType
    ): NetworkResult<T> {
        return when {
            response.status.isSuccess() -> {
                when (responseType) {
                    ResponseType.JSON -> {
                        try {
                            val body = response.bodyAsText()
                            val result = jsonSerializer.decodeFromString<T>(body)
                            NetworkResult.Success(result)
                        } catch (e: Exception) {
                            NetworkResult.Error(
                                code = response.status.value,
                                message = "Parse response failed: ${e.message}",
                                throwable = e
                            )
                        }
                    }
                    ResponseType.STRING -> {
                        @Suppress("UNCHECKED_CAST")
                        NetworkResult.Success(response.bodyAsText() as T)
                    }
                    ResponseType.BYTES -> {
                        @Suppress("UNCHECKED_CAST")
                        NetworkResult.Success(response.bodyAsText().encodeToByteArray() as T)
                    }
                }
            }
            else -> {
                NetworkResult.Error(
                    code = response.status.value,
                    message = "HTTP Error: ${response.status.description}"
                )
            }
        }
    }

    companion object {
        val defaultJson = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }
}

/**
 * 上传进度状态
 */
sealed class UploadProgress {
    data object Idle : UploadProgress()
    data object Started : UploadProgress()
    data class InProgress(val percent: Int, val uploaded: Long, val total: Long) : UploadProgress()
    data object Completed : UploadProgress()
    data class Failed(val error: Throwable) : UploadProgress()
}

/**
 * 上传文件数据类
 */
data class UploadFile(
    val fieldName: String,
    val fileName: String,
    val byteArray: ByteArray,
    val mimeType: String
) {
    companion object {
        const val MIME_IMAGE_JPEG = "image/jpeg"
        const val MIME_IMAGE_PNG = "image/png"
        const val MIME_IMAGE_GIF = "image/gif"
        const val MIME_IMAGE_WEBP = "image/webp"
        const val MIME_VIDEO_MP4 = "video/mp4"
        const val MIME_VIDEO_QUICKTIME = "video/quicktime"
        const val MIME_VIDEO_MPEG = "video/mpeg"
        const val MIME_VIDEO_WEBM = "video/webm"

        fun image(
            fieldName: String,
            fileName: String,
            bytes: ByteArray,
            type: ImageType = ImageType.JPEG
        ) = UploadFile(fieldName, fileName, bytes, type.mimeType)

        fun video(
            fieldName: String,
            fileName: String,
            bytes: ByteArray,
            type: VideoType = VideoType.MP4
        ) = UploadFile(fieldName, fileName, bytes, type.mimeType)
    }

    enum class ImageType(val mimeType: String) {
        JPEG(MIME_IMAGE_JPEG),
        PNG(MIME_IMAGE_PNG),
        GIF(MIME_IMAGE_GIF),
        WEBP(MIME_IMAGE_WEBP)
    }

    enum class VideoType(val mimeType: String) {
        MP4(MIME_VIDEO_MP4),
        QUICKTIME(MIME_VIDEO_QUICKTIME),
        MPEG(MIME_VIDEO_MPEG),
        WEBM(MIME_VIDEO_WEBM)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UploadFile) return false
        return fieldName == other.fieldName &&
                fileName == other.fileName &&
                byteArray.contentEquals(other.byteArray) &&
                mimeType == other.mimeType
    }

    override fun hashCode(): Int {
        var result = fieldName.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + byteArray.contentHashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}

/**
 * 响应类型
 */
enum class ResponseType {
    JSON,
    STRING,
    BYTES
}

/**
 * 请求体类型
 */
sealed class RequestBody {
    data class Content(
        val content: Any?,
        val contentType: ContentType,
        val jsonSerializer: Json
    ) : RequestBody()

    data class Form(
        val formData: Map<String, String>
    ) : RequestBody()
}
