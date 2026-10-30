package com.juanpaxi.sini.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.juanpaxi.sini.core.designsystem.component.SiniBackground
import com.juanpaxi.sini.core.designsystem.component.SiniNavigationBar
import com.juanpaxi.sini.core.designsystem.component.SiniNavigationBarItem
import com.juanpaxi.sini.core.designsystem.component.SiniTopAppBar
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons
import com.juanpaxi.sini.core.navigation.Navigator
import com.juanpaxi.sini.core.navigation.toEntries
import com.juanpaxi.sini.feature.home.navigation.HomeNavKey
import com.juanpaxi.sini.feature.home.navigation.homeEntry
import com.juanpaxi.sini.navigation.TOP_LEVEL_NAV_ITEMS

@Composable
fun SiniApp(
    appState: SiniAppState,
    modifier: Modifier = Modifier,
) {
    SiniBackground(modifier = modifier) {
        SiniApp(
            appState = appState,
            onTopAppBarActionClick = {},
        )
    }
}

@Composable
internal fun SiniApp(
    appState: SiniAppState,
    onTopAppBarActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator = remember { Navigator(appState.navigationState) }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            SiniNavigationBar(
                currentTopLevelKey = appState.navigationState.currentTopLevelKey,
                onNavigate = navigator::navigate,
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding),
        ) {
            val entryProvider =
                entryProvider {
                    homeEntry(navigator)
                }

            NavDisplay(
                entries = appState.navigationState.toEntries(entryProvider),
                onBack = { navigator.goBack() },
            )
            when (appState.navigationState.currentKey) {
                HomeNavKey -> {
                    SiniTopAppBar(
                        navigationIcon = SiniIcons.Sini,
                        navigationIconContentDescription = "Sini",
                        actionIcon = SiniIcons.Notification,
                        actionIconContentDescription = "Notification",
                    )
                }
            }
        }
    }
}

@Composable
fun SiniNavigationBar(
    currentTopLevelKey: NavKey,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    SiniNavigationBar(
        modifier = modifier,
    ) {
        TOP_LEVEL_NAV_ITEMS.forEach { (navKey, navItem) ->
            val selected = navKey == currentTopLevelKey

            SiniNavigationBarItem(
                selected = selected,
                onClick = { onNavigate(navKey) },
                icon = {
                    Icon(
                        painter = painterResource(navItem.unselectedIcon),
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        painter = painterResource(navItem.selectedIcon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                },
                label = {
                    Text(
                        text = stringResource(navItem.iconTextId),
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
            )
        }
    }
}
