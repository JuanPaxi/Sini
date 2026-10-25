package com.juanpaxi.sini.core.database.di

import android.content.Context
import androidx.room3.Room
import com.juanpaxi.sini.core.database.RoomTransactionRunner
import com.juanpaxi.sini.core.database.SiniDatabase
import com.juanpaxi.sini.core.database.TransactionRunner
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesSiniDatabase(
        @ApplicationContext context: Context,
    ): SiniDatabase =
        Room
            .databaseBuilder(
                context,
                SiniDatabase::class.java,
                "sini-database",
            ).build()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class TransactionModule {
    @Binds
    internal abstract fun bindsRoomTransactionRunner(roomTransactionRunner: RoomTransactionRunner): TransactionRunner
}
