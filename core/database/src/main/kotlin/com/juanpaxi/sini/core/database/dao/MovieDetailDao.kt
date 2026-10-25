package com.juanpaxi.sini.core.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.juanpaxi.sini.core.database.model.MovieDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailDao {
    @Upsert
    suspend fun upsert(movieDetail: MovieDetailEntity)

    @Query(
        """
        SELECT * FROM movie_details
        WHERE tmdbId = :tmdbId
    """,
    )
    suspend fun movieDetail(tmdbId: Int): MovieDetailEntity?

    @Query(
        """
        UPDATE movie_details
        SET isFavorite = :isFavorite
        WHERE tmdbId = :movieId
    """,
    )
    suspend fun updateFavorite(
        movieId: Int,
        isFavorite: Boolean,
    )

    @Query(
        """
        SELECT * FROM movie_details
        WHERE isFavorite = 1
        ORDER BY title
    """,
    )
    fun favorites(): Flow<List<MovieDetailEntity>>
}
