package com.juanpaxi.sini.core.network.demo

import JvmUnitTestDemoAssetManager
import com.juanpaxi.sini.core.network.retrofit.RetrofitSiniNetworkApi
import kotlinx.serialization.json.Json
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlin.jvm.java

abstract class SiniNetworkTestBase {
    lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    protected lateinit var service: RetrofitSiniNetworkApi

    private val networkJson =
        Json {
            ignoreUnknownKeys = true
        }

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        okHttpClient =
            OkHttpClient
                .Builder()
                .build()
        service = createService()
    }

    @After
    fun tearDown() {
        mockWebServer.close()
    }

    private fun readJsonFile(fileName: String): String =
        JvmUnitTestDemoAssetManager.open(fileName).use { inputStream ->
            inputStream.bufferedReader().readText()
        }

    protected fun enqueueResponse(fileName: String) {
        val json = readJsonFile(fileName)

        val response =
            MockResponse
                .Builder()
                .code(200)
                .setHeader("Content-Type", "application/json")
                .body(json)
                .build()

        mockWebServer.enqueue(response)
    }

    protected fun enqueueHttpError(code: Int) {
        val response =
            MockResponse
                .Builder()
                .code(code)
                .build()

        mockWebServer.enqueue(response)
    }

    protected fun createService(): RetrofitSiniNetworkApi =
        Retrofit
            .Builder()
            .baseUrl(mockWebServer.url("/"))
            .callFactory(okHttpClient)
            .addConverterFactory(
                networkJson.asConverterFactory(
                    "application/json".toMediaType(),
                ),
            ).build()
            .create(RetrofitSiniNetworkApi::class.java)
}
