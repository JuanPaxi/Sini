package com.juanpaxi.sini.core.database.dao

import android.content.Context
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import com.juanpaxi.sini.core.database.SiniDatabase
import org.junit.After
import org.junit.Before

internal abstract class DatabaseTest {
    protected lateinit var db: SiniDatabase
    protected lateinit var movieDao: MovieDao
    protected lateinit var movieDetailDao: MovieDetailDao
    protected lateinit var remoteKeyDao: RemoteKeyDao

    @Before
    fun setup() {
        db =
            run {
                val context = ApplicationProvider.getApplicationContext<Context>()
                Room
                    .inMemoryDatabaseBuilder(
                        context,
                        SiniDatabase::class.java,
                    ).build()
            }
        movieDao = db.movieDao()
        movieDetailDao = db.movieDetailDao()
        remoteKeyDao = db.remoteKeyDao()
    }

    @After
    fun teardown() = db.close()
}
