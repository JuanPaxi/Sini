package com.juanpaxi.sini.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.juanpaxi.sini.core.navigation.Navigator
import com.juanpaxi.sini.feature.detail.navigation.navigateToMovieDetail
import com.juanpaxi.sini.feature.home.HomeScreen

fun EntryProviderScope<NavKey>.homeEntry(navigator: Navigator) {
    entry<HomeNavKey> {
        HomeScreen(
            onMovieDetailClick = navigator::navigateToMovieDetail,
        )
    }
}
