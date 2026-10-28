package com.juanpaxi.sini.core.database

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.juanpaxi.sini.core.database.dao.MovieDao
import com.juanpaxi.sini.core.database.dao.MovieDetailDao
import com.juanpaxi.sini.core.database.dao.RemoteKeyDao
import com.juanpaxi.sini.core.database.model.MovieDetailEntity
import com.juanpaxi.sini.core.database.model.MovieEntity
import com.juanpaxi.sini.core.database.model.RemoteKeyEntity
import com.juanpaxi.sini.core.database.util.GenreConverter
import com.juanpaxi.sini.core.database.util.InstantConverter
import com.juanpaxi.sini.core.database.util.LocalDateConverter
import com.juanpaxi.sini.core.database.util.UuidConverter

@Database(
    entities = [
        MovieEntity::class,
        RemoteKeyEntity::class,
        MovieDetailEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@ColumnTypeConverters(
    LocalDateConverter::class,
    UuidConverter::class,
    InstantConverter::class,
    GenreConverter::class,
)
abstract class SiniDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    abstract fun remoteKeyDao(): RemoteKeyDao

    abstract fun movieDetailDao(): MovieDetailDao
}
