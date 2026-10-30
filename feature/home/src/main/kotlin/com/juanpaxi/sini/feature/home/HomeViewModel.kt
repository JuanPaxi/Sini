package com.juanpaxi.sini.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.juanpaxi.sini.core.data.MoviesRepository
import com.juanpaxi.sini.core.data.Synchronizer
import com.juanpaxi.sini.core.data.util.NetworkMonitor
import com.juanpaxi.sini.core.model.data.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val moviesRepository: MoviesRepository,
        private val synchronizer: Synchronizer,
        networkMonitor: NetworkMonitor,
    ) : ViewModel() {
        private var canUpdatePages: Boolean = false
        private var initialCacheSize: Int = 0
        private val requestedPages: MutableSet<Int> = mutableSetOf()

        val isOnline: Flow<Boolean> = networkMonitor.isOnline
        val popularMovies: Flow<PagingData<Movie>> =
            moviesRepository
                .getPopularMovies()
                .cachedIn(viewModelScope)

        init {
            viewModelScope.launch {
                initialCacheSize = moviesRepository.countMovies()
                moviesRepository
                    .observeLastUpdated()
                    .collect { lastUpdated ->
                        canUpdatePages = lastUpdated == null || Clock.System.now() - lastUpdated >= CACHE_TIMEOUT
                    }
            }
        }

        fun onVisibleItemIndexChanged(index: Int) {
            if (!canUpdatePages) return
            if ((index % PAGE_SIZE) >= PAGE_UPDATE_THRESHOLD) {
                val nextPage = (index / PAGE_SIZE) + 2
                val pageExists = nextPage * PAGE_SIZE <= initialCacheSize
                if (pageExists && requestedPages.add(nextPage)) {
                    viewModelScope.launch {
                        try {
                            synchronizer.sync(nextPage)
                        } catch (e: Exception) {
                            requestedPages.remove(nextPage)
                        }
                    }
                }
            }
        }

        companion object {
            private const val PAGE_SIZE = 20
            private const val PAGE_UPDATE_THRESHOLD = 16
            private val CACHE_TIMEOUT = 1.hours
        }
    }
