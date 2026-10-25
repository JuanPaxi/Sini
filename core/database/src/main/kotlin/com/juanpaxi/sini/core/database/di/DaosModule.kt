package com.juanpaxi.sini.core.database.di

import com.juanpaxi.sini.core.database.SiniDatabase
import com.juanpaxi.sini.core.database.dao.MovieDao
import com.juanpaxi.sini.core.database.dao.MovieDetailDao
import com.juanpaxi.sini.core.database.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesMoviesDao(database: SiniDatabase): MovieDao = database.movieDao()

    @Provides
    fun providesRemoteKeyDao(database: SiniDatabase): RemoteKeyDao = database.remoteKeyDao()

    @Provides
    fun providesMovieDetailDao(database: SiniDatabase): MovieDetailDao = database.movieDetailDao()
}
