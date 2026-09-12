package com.juanpaxi.sini.core.network.di

import android.content.Context
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.util.DebugLogger
import com.juanpaxi.sini.core.network.BuildConfig
import com.juanpaxi.sini.core.network.demo.DemoAssetManager
import com.juanpaxi.sini.core.network.interceptor.SiniHttpRequestInterceptor
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

private const val API_KEY = BuildConfig.API_KEY

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun providesNetworkJson(): Json =
        Json {
            ignoreUnknownKeys = true
        }

    @Provides
    @Singleton
    fun providesDemoAssetManager(
        @ApplicationContext context: Context,
    ): DemoAssetManager = DemoAssetManager(context.assets::open)

    @Provides
    @Singleton
    fun provideOkHttpCallFactory(): Call.Factory =
        OkHttpClient
            .Builder()
            .addInterceptor(SiniHttpRequestInterceptor(API_KEY))
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) setLevel(HttpLoggingInterceptor.Level.BODY)
                    },
            ).build()

    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactory: Lazy<Call.Factory>,
        @ApplicationContext application: Context,
    ): ImageLoader =
        ImageLoader
            .Builder(application)
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = { okHttpCallFactory.get() }))
            }.apply {
                if (BuildConfig.DEBUG) logger(DebugLogger())
            }.build()
}
