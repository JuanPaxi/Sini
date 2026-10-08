package com.juanpaxi.sini.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

private const val TMDB_API_HOST = "api.themoviedb.org"

class SiniHttpRequestInterceptor(
    private val apiKey: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.url.host == TMDB_API_HOST) {
            val url =
                originalRequest.url
                    .newBuilder()
                    .addQueryParameter("api_key", apiKey)
                    .build()

            val request =
                originalRequest
                    .newBuilder()
                    .url(url)
                    .build()

            return chain.proceed(request)
        }

        return chain.proceed(originalRequest)
    }
}
