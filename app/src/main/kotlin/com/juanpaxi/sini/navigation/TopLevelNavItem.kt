package com.juanpaxi.sini.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons
import com.juanpaxi.sini.feature.bookmark.navigation.BookmarkNavKey
import com.juanpaxi.sini.feature.home.navigation.HomeNavKey
import com.juanpaxi.sini.feature.profile.navigation.ProfileNavKey
import com.juanpaxi.sini.feature.bookmark.R as bookmarkR
import com.juanpaxi.sini.feature.home.R as homeR
import com.juanpaxi.sini.feature.profile.R as profileR

data class TopLevelNavItem(
    @param:DrawableRes val selectedIcon: Int,
    @param:DrawableRes val unselectedIcon: Int,
    @param:StringRes val iconTextId: Int,
    @param:StringRes val titleTextId: Int,
)

val HOME =
    TopLevelNavItem(
        selectedIcon = SiniIcons.Home,
        unselectedIcon = SiniIcons.HomeBorder,
        iconTextId = homeR.string.home_api_title,
        titleTextId = homeR.string.home_api_title,
    )

val PROFILE =
    TopLevelNavItem(
        selectedIcon = SiniIcons.Profile,
        unselectedIcon = SiniIcons.ProfileBorder,
        iconTextId = profileR.string.profile_api_title,
        titleTextId = profileR.string.profile_api_title,
    )

val BOOKMARK =
    TopLevelNavItem(
        selectedIcon = SiniIcons.Archive,
        unselectedIcon = SiniIcons.ArchiveBorder,
        iconTextId = bookmarkR.string.bookmarks_api_title,
        titleTextId = bookmarkR.string.bookmarks_api_title,
    )

val TOP_LEVEL_NAV_ITEMS =
    mapOf(
        HomeNavKey to HOME,
        BookmarkNavKey to BOOKMARK,
        ProfileNavKey to PROFILE,
    )
