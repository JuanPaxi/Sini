package com.juanpaxi.sini.feature.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpaxi.sini.core.data.MovieDetailRepository
import com.juanpaxi.sini.core.model.data.MovieDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel
    @Inject
    constructor(
        movieDetailRepository: MovieDetailRepository,
    ) : ViewModel() {
        val uiState: StateFlow<BookmarkUiState> =
            movieDetailRepository
                .observeFavorites()
                .map { bookmarks ->
                    if (bookmarks.isEmpty()) {
                        BookmarkUiState.Empty
                    } else {
                        BookmarkUiState.Success(bookmarks)
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = BookmarkUiState.Loading,
                )
    }

sealed interface BookmarkUiState {
    data object Loading : BookmarkUiState

    data object Empty : BookmarkUiState

    data class Success(
        val bookmarks: List<MovieDetail>,
    ) : BookmarkUiState
}
