package com.juanpaxi.sini.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons
import com.juanpaxi.sini.feature.home.navigation.HomeNavKey
import com.juanpaxi.sini.feature.home.R as homeR

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

val TOP_LEVEL_NAV_ITEMS =
    mapOf(
        HomeNavKey to HOME,
    )
