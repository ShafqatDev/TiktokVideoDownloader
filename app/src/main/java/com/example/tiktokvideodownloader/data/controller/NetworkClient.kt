package com.example.tiktokvideodownloader.data.controller

import android.util.Log
import com.example.tiktokvideodownloader.data.response.NetworkResponse
import com.example.tiktokvideodownloader.data.response.RequestTypes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

object NetworkClient {

    val jsonHelper = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(jsonHelper)
        }
    }

    suspend inline fun <reified T> makeNetworkRequest(
        url: String,
        requestType: RequestTypes,
        headers: Map<String, String>? = null,
    ): NetworkResponse<T> {
        Log.d("makeNetworkRequest", "makeNetworkRequest: $url")
        return withContext(Dispatchers.IO) {
            try {
                val response: String = requestType.getHttpBuilder(url) {
                    Log.d("makeNetworkRequest", "request builder${url}")

                    if (requestType is RequestTypes.Post) {
                        it.setBody(requestType.body)
                    }
                    headers?.let { headers ->
                        headers.forEach { (key, value) ->
                            it.header(key, value)
                        }
                    }
                    Log.d("cvv", "${it.body}")
                }.body()
                Log.d("makeNetworkRequest", "Network Response:$response")


                val newResponse: T = jsonHelper.decodeFromString(response)
                (NetworkResponse.Success(newResponse))

            } catch (e: ClientRequestException) {
                Log.d("makeNetworkRequest", "Network ClientRequestException:${e.message}")

                (NetworkResponse.Failure(e.message))
            } catch (e: ServerResponseException) {
                Log.d("makeNetworkRequest", "Network ServerResponseException:${e.message}")

                (NetworkResponse.Failure(e.message))
            } catch (e: Exception) {
                Log.d("makeNetworkRequest", "Network Exception:${e.message}")
                (NetworkResponse.Failure(e.message ?: "Unknown error"))
            }
        }
    }

    suspend fun RequestTypes.getHttpBuilder(
        url: String,
        callback: (HttpRequestBuilder) -> Unit
    ): HttpResponse {
        return when (this) {
            is RequestTypes.Get -> {
                client.get(url) {
                    callback.invoke(this)
                }
            }

            is RequestTypes.Post -> {
                client.post(url) {
                    callback.invoke(this)
                    setBody(body)
                }
            }
        }
    }
}