package com.juanpaxi.sini.core.database.dao

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Query
import androidx.room3.Upsert
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter
import com.juanpaxi.sini.core.database.model.MovieEntity

@Dao
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
interface MovieDao {
    @Upsert
    suspend fun upsertAll(movies: List<MovieEntity>)

    @Query(
        """
        SELECT * FROM movies
        ORDER BY position
    """,
    )
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query(
        """
        SELECT * FROM movies
        WHERE position >= :startPosition AND position <= :endPosition
        ORDER BY position
    """,
    )
    suspend fun moviesInPositionRange(
        startPosition: Int,
        endPosition: Int,
    ): List<MovieEntity>

    @Query(
        """
        DELETE FROM movies
        WHERE position >= :startPosition AND position <= :endPosition
    """,
    )
    suspend fun deleteMovies(
        startPosition: Int,
        endPosition: Int,
    )

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun countMovies(): Int
}
