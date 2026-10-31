package com.juanpaxi.sini.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.juanpaxi.sini.core.designsystem.component.AppendError
import com.juanpaxi.sini.core.designsystem.component.AppendLoading
import com.juanpaxi.sini.core.designsystem.component.InitialError
import com.juanpaxi.sini.core.designsystem.component.Loading
import com.juanpaxi.sini.core.designsystem.component.MovieCard
import com.juanpaxi.sini.core.designsystem.component.MovieCardPlaceholder
import com.juanpaxi.sini.core.designsystem.component.RefreshError

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onMovieDetailClick: (Int) -> Unit,
) {
    val movies = viewModel.popularMovies.collectAsLazyPagingItems()
    val isOnline by viewModel.isOnline.collectAsState(initial = false)
    val gridState = rememberLazyGridState()
    val loadState = movies.loadState
    val isEmpty = movies.itemCount == 0
    val isInitialLoading = isEmpty && loadState.refresh is LoadState.Loading
    val isInitialError = isEmpty && loadState.mediator?.refresh is LoadState.Error
    val showRefreshError = !isEmpty && loadState.mediator?.refresh is LoadState.Error

    LaunchedEffect(gridState, isOnline) {
        snapshotFlow {
            gridState.firstVisibleItemIndex
        }.collect { index ->
            if (isOnline) viewModel.onVisibleItemIndexChanged(index)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.isOnline.collect { isOnline ->
            val hasPagingError =
                movies.loadState.mediator?.refresh is LoadState.Error ||
                    movies.loadState.mediator?.append is LoadState.Error
            if (isOnline && hasPagingError) movies.retry()
        }
    }

    when {
        isInitialLoading -> {
            Loading(title = "Loading Movies ...")
        }

        isInitialError -> {
            InitialError(retry = movies::retry)
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                contentPadding =
                    PaddingValues(
                        top = 12.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 16.dp,
                    ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (showRefreshError) {
                    item(
                        span = { GridItemSpan(maxLineSpan) },
                    ) {
                        RefreshError(retry = movies::retry)
                    }
                }
                items(
                    count = movies.itemCount,
                ) { index ->
                    val movie = movies[index]
                    if (movie != null) {
                        MovieCard(
                            movie = movie,
                            onMovieDetailClick = {
                                onMovieDetailClick(movie.id)
                            },
                        )
                    } else {
                        MovieCardPlaceholder()
                    }
                }

                if (loadState.append is LoadState.Error) {
                    item(
                        span = { GridItemSpan(maxLineSpan) },
                    ) {
                        AppendError(retry = movies::retry)
                    }
                }
                if (loadState.append is LoadState.Loading) {
                    item(
                        span = { GridItemSpan(maxLineSpan) },
                    ) {
                        AppendLoading()
                    }
                }
            }
        }
    }
}
