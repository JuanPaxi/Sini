package com.juanpaxi.sini.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpaxi.sini.core.data.MovieDetailRepository
import com.juanpaxi.sini.core.model.data.MovieDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

@HiltViewModel(assistedFactory = MovieDetailViewModel.Factory::class)
class MovieDetailViewModel
    @AssistedInject
    constructor(
        private val repository: MovieDetailRepository,
        @Assisted val movieId: Int,
    ) : ViewModel() {
        private val _movieDetail = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
        val movieDetail = _movieDetail.asStateFlow()

        init {
            loadMovieDetail()
        }

        fun loadMovieDetail() {
            viewModelScope.launch {
                _movieDetail.value = MovieDetailUiState.Loading
                try {
                    val movie = repository.getMovieDetail(movieId = movieId)
                    _movieDetail.value = MovieDetailUiState.Success(movie)
                } catch (e: IOException) {
                    _movieDetail.value = MovieDetailUiState.Error
                }
            }
        }

        fun retry() = loadMovieDetail()

        fun toggleFavorite() {
            val state = _movieDetail.value
            if (state is MovieDetailUiState.Success) {
                val movie = state.movie
                val newFavorite = !movie.isFavorite
                viewModelScope.launch {
                    repository.setFavorite(
                        movieId = movie.id,
                        isFavorite = newFavorite,
                    )
                    _movieDetail.value =
                        MovieDetailUiState.Success(
                            movie =
                                movie.copy(
                                    isFavorite = newFavorite,
                                ),
                        )
                }
            }
        }

        @AssistedFactory
        interface Factory {
            fun create(movieId: Int): MovieDetailViewModel
        }
    }

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState

    data class Success(
        val movie: MovieDetail,
    ) : MovieDetailUiState

    data object Error : MovieDetailUiState
}
