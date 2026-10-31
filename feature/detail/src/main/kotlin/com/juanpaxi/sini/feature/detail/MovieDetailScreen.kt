package com.juanpaxi.sini.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juanpaxi.sini.core.designsystem.component.Error
import com.juanpaxi.sini.core.designsystem.component.GenreItem
import com.juanpaxi.sini.core.designsystem.component.Loading
import com.juanpaxi.sini.core.designsystem.component.MovieArtwork
import com.juanpaxi.sini.core.model.data.MovieDetail

@Composable
fun MovieDetailScreen(
    onBackClick: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.movieDetail.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (uiState) {
            is MovieDetailUiState.Loading -> {
                Loading(title = "Loading Movie ...")
            }

            is MovieDetailUiState.Success -> {
                MovieDetailContent(
                    movie = (uiState as MovieDetailUiState.Success).movie,
                    toggleFavorite = viewModel::toggleFavorite,
                    onBackClick = onBackClick,
                )
            }

            is MovieDetailUiState.Error -> {
                Error(
                    title = "Couldn't load movie",
                    description = "Something went wrong while loading this movie.",
                    onRetry = viewModel::retry,
                )
            }
        }
    }
}

@Composable
private fun MovieDetailContent(
    movie: MovieDetail,
    toggleFavorite: () -> Unit,
    onBackClick: () -> Unit,
) {
    BoxWithConstraints {
        val artworkHeight = maxHeight * 0.7f
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            MovieArtwork(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(artworkHeight),
                backdropPath = movie.backdropPath,
                posterPath = movie.posterPath,
                title = movie.title,
                isFavorite = movie.isFavorite,
                voteAverage = movie.voteAverage,
                runtime = movie.runtime,
                releaseDate = movie.releaseDate,
                toggleFavorite = toggleFavorite,
                onBackClick = onBackClick,
            )
            Spacer(modifier = Modifier.height(24.dp))
            MovieGenres(genres = movie.genres)
            Spacer(modifier = Modifier.height(8.dp))
            MovieOverview(overview = movie.overview)
        }
    }
}

@Composable
fun MovieGenres(genres: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally,
            ),
    ) {
        genres.forEach { genre ->
            GenreItem(name = genre)
        }
    }
}

@Composable
fun MovieOverview(
    overview: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = overview.orEmpty().ifBlank { "No overview available." },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
