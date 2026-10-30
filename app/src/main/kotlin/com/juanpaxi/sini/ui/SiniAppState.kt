package com.juanpaxi.sini.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.juanpaxi.sini.core.navigation.NavigationState
import com.juanpaxi.sini.core.navigation.rememberNavigationState
import com.juanpaxi.sini.feature.home.navigation.HomeNavKey
import com.juanpaxi.sini.navigation.TOP_LEVEL_NAV_ITEMS
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberSiniAppState(coroutineScope: CoroutineScope = rememberCoroutineScope()): SiniAppState {
    val navigationState = rememberNavigationState(HomeNavKey, TOP_LEVEL_NAV_ITEMS.keys)
    return remember(
        navigationState,
        coroutineScope,
    ) {
        SiniAppState(
            navigationState = navigationState,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class SiniAppState(
    val navigationState: NavigationState,
    coroutineScope: CoroutineScope,
)
