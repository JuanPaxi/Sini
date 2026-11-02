package com.juanpaxi.sini.feature.bookmark.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.juanpaxi.sini.core.navigation.Navigator
import com.juanpaxi.sini.feature.bookmark.BookmarkScreen
import com.juanpaxi.sini.feature.detail.navigation.navigateToMovieDetail

fun EntryProviderScope<NavKey>.bookmarkEntry(navigator: Navigator) {
    entry<BookmarkNavKey> {
        BookmarkScreen(
            onMovieDetailClick = navigator::navigateToMovieDetail,
        )
    }
}
