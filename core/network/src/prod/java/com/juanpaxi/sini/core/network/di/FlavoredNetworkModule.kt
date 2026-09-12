package com.juanpaxi.sini.core.network.di

import com.juanpaxi.sini.core.network.SiniNetworkDataSource
import com.juanpaxi.sini.core.network.retrofit.RetrofitSiniNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {
    @Binds
    fun binds(impl: RetrofitSiniNetwork): SiniNetworkDataSource
}
