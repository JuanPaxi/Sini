package com.juanpaxi.sini.feature.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juanpaxi.sini.core.designsystem.component.EmptyBookmarks
import com.juanpaxi.sini.core.designsystem.component.Loading
import com.juanpaxi.sini.core.designsystem.component.MovieCard

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    onMovieDetailClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        BookmarkUiState.Loading -> {
            Loading(title = "Loading your favorites...")
        }

        BookmarkUiState.Empty -> {
            EmptyBookmarks()
        }

        is BookmarkUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp, 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = state.bookmarks,
                ) { movieDetail ->
                    MovieCard(
                        movieId = movieDetail.id,
                        title = movieDetail.title,
                        posterPath = movieDetail.posterPath,
                        voteAverage = movieDetail.voteAverage,
                        onMovieDetailClick = onMovieDetailClick,
                    )
                }
            }
        }
    }
}
