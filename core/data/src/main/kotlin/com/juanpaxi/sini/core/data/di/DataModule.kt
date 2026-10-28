package com.juanpaxi.sini.core.data.di

import com.juanpaxi.sini.core.data.MovieDetailRepository
import com.juanpaxi.sini.core.data.MoviesRepository
import com.juanpaxi.sini.core.data.Synchronizer
import com.juanpaxi.sini.core.data.UserDataRepository
import com.juanpaxi.sini.core.data.repository.MovieSynchronizer
import com.juanpaxi.sini.core.data.repository.OfflineFirstMovieDetailRepository
import com.juanpaxi.sini.core.data.repository.OfflineFirstMoviesRepository
import com.juanpaxi.sini.core.data.repository.OfflineFirstUserDataRepository
import com.juanpaxi.sini.core.data.util.ConnectivityManagerNetworkMonitor
import com.juanpaxi.sini.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsOfflineFirstMoviesRepository(impl: OfflineFirstMoviesRepository): MoviesRepository

    @Binds
    internal abstract fun bindsOfflineFirstUserDataRepository(impl: OfflineFirstUserDataRepository): UserDataRepository

    @Binds
    internal abstract fun bindsOfflineFirstMovieDetailRepository(impl: OfflineFirstMovieDetailRepository): MovieDetailRepository

    @Binds
    internal abstract fun bindsMovieSynchronizer(impl: MovieSynchronizer): Synchronizer

    @Binds
    internal abstract fun bindsNetworkMonitor(impl: ConnectivityManagerNetworkMonitor): NetworkMonitor
}
