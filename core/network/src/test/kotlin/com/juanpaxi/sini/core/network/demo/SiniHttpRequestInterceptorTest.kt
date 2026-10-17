package com.juanpaxi.sini.core.network.demo

import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.network.interceptor.SiniHttpRequestInterceptor
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SiniHttpRequestInterceptorTest {
    private lateinit var interceptor: SiniHttpRequestInterceptor
    private lateinit var chain: Interceptor.Chain

    private val apiKey = "FAKE_API_KEY"

    @Before
    fun setup() {
        chain = mock()
        interceptor = SiniHttpRequestInterceptor(apiKey)
    }

    @Test
    fun addsApiKey_whenRequestIsForTmdb() {
        val request =
            Request
                .Builder()
                .url("https://api.themoviedb.org/fake-endpoint")
                .build()

        whenever(chain.request()).thenReturn(request)
        whenever(chain.proceed(any())).thenReturn(mock())

        interceptor.intercept(chain)

        val requestCaptor = argumentCaptor<Request>()
        verify(chain).proceed(requestCaptor.capture())

        val interceptedRequest = requestCaptor.firstValue

        assertThat(
            interceptedRequest.url.queryParameter("api_key"),
        ).isEqualTo(apiKey)
    }

    @Test
    fun doesNotAddApiKey_whenRequestIsNotForTmdb() {
        val request =
            Request
                .Builder()
                .url("https://example.com/fake-endpoint")
                .build()

        whenever(chain.request()).thenReturn(request)
        whenever(chain.proceed(any())).thenReturn(mock())

        interceptor.intercept(chain)

        val requestCaptor = argumentCaptor<Request>()
        verify(chain).proceed(requestCaptor.capture())

        val interceptedRequest = requestCaptor.firstValue

        assertThat(
            interceptedRequest.url.queryParameter("api_key"),
        ).isNull()
    }
}
