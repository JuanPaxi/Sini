package com.juanpaxi.sini.core.data.model

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.juanpaxi.sini.core.database.model.MovieDetailEntity
import com.juanpaxi.sini.core.database.model.MovieEntity
import com.juanpaxi.sini.core.model.data.Movie
import com.juanpaxi.sini.core.model.data.MovieDetail
import com.juanpaxi.sini.core.network.model.NetworkGenre
import com.juanpaxi.sini.core.network.model.NetworkMovie
import com.juanpaxi.sini.core.network.model.NetworkMovieDetail
import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.uuid.Uuid

class NetworkEntityTest {
    @Test
    fun networkMovieMapsToEntity() {
        val networkMovie =
            NetworkMovie(
                id = 123,
                title = "Test Movie",
                posterPath = null,
                voteAverage = 7.568,
            )
        val entity = networkMovie.asEntity(position = 20)
        assertThat(entity.tmdbId).isEqualTo(123)
        assertThat(entity.title).isEqualTo("Test Movie")
        assertThat(entity.posterPath).isNull()
        assertThat(entity.voteAverage).isEqualTo(7.568)
        assertThat(entity.position).isEqualTo(20)
    }

    @Test
    fun networkMovieMapsToEntityWithExistingLocalId() {
        val localId = Uuid.random()
        val networkMovie =
            NetworkMovie(
                id = 123,
                title = "Test Movie",
                posterPath = "/poster.jpg",
                voteAverage = 7.5,
            )
        val entity =
            networkMovie.asEntity(
                position = 20,
                localId = localId,
            )
        assertThat(entity.localId).isEqualTo(localId)
    }

    @Test
    fun movieEntityMapsToExternalModel() {
        val entity =
            MovieEntity(
                localId = Uuid.random(),
                tmdbId = 123,
                position = 20,
                title = "Test Movie",
                posterPath = null,
                voteAverage = 7.5,
            )
        val expected =
            Movie(
                id = 123,
                title = "Test Movie",
                posterPath = null,
                voteAverage = 7.5,
            )
        assertThat(entity.asExternalModel()).isEqualTo(expected)
    }

    @Test
    fun networkMovieDetailMapsToEntity() {
        val networkModel =
            NetworkMovieDetail(
                id = 123,
                title = "Test Movie",
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg",
                overview = "Test overview",
                voteAverage = 7.5,
                releaseDate = "2026-10-25",
                runtime = 120,
                genres =
                    listOf(
                        NetworkGenre(id = 28, name = "Action"),
                        NetworkGenre(id = 12, name = "Adventure"),
                    ),
            )

        val expected =
            MovieDetailEntity(
                tmdbId = 123,
                title = "Test Movie",
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg",
                overview = "Test overview",
                voteAverage = 7.5,
                releaseDate = LocalDate.parse("2026-10-25"),
                runtime = 120,
                genres = listOf("Action", "Adventure"),
                isFavorite = true,
            )

        assertThat(networkModel.asEntity(isFavorite = true)).isEqualTo(expected)
    }

    @Test
    fun networkMovieDetailMapsBlankOrNullReleaseDateToNull() {
        val releaseDates = listOf(null, "", "   ")

        releaseDates.forEach { releaseDate ->
            val networkModel =
                NetworkMovieDetail(
                    id = 123,
                    title = "Test Movie",
                    voteAverage = 7.5,
                    releaseDate = releaseDate,
                    genres = emptyList(),
                )

            val entity = networkModel.asEntity(isFavorite = false)

            assertWithMessage("releaseDate = '$releaseDate'")
                .that(entity.releaseDate)
                .isNull()
        }
    }

    @Test
    fun movieDetailEntityMapsToExternalModel() {
        val entity =
            MovieDetailEntity(
                tmdbId = 123,
                title = "Test Movie",
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg",
                overview = "Test overview",
                voteAverage = 7.5,
                releaseDate = LocalDate.parse("2026-10-25"),
                runtime = 120,
                genres = listOf("Action", "Adventure"),
                isFavorite = true,
            )

        val expected =
            MovieDetail(
                id = 123,
                title = "Test Movie",
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg",
                overview = "Test overview",
                voteAverage = 7.5,
                releaseDate = LocalDate.parse("2026-10-25"),
                runtime = 120,
                genres = listOf("Action", "Adventure"),
                isFavorite = true,
            )

        assertThat(entity.asExternalModel()).isEqualTo(expected)
    }
}
