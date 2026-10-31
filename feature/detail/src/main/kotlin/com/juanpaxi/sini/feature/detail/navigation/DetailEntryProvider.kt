package com.juanpaxi.sini.feature.detail.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.juanpaxi.sini.core.navigation.Navigator
import com.juanpaxi.sini.feature.detail.MovieDetailScreen
import com.juanpaxi.sini.feature.detail.MovieDetailViewModel

fun EntryProviderScope<NavKey>.detailEntry(navigator: Navigator) {
    entry<DetailNavKey> { key ->
        MovieDetailScreen(
            onBackClick = navigator::goBack,
            viewModel =
                hiltViewModel<MovieDetailViewModel, MovieDetailViewModel.Factory> { factory ->
                    factory.create(key.movieId)
                },
        )
    }
}
