package com.juanpaxi.sini.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons
import com.juanpaxi.sini.core.designsystem.theme.SiniTheme

@Composable
fun SiniNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        tonalElevation = 0.dp,
        modifier = modifier,
        content = content,
    )
}

@Composable
fun RowScope.SiniNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
    selectedLabel: @Composable (() -> Unit)? = label,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = if (selected) selectedLabel else label,
        alwaysShowLabel = alwaysShowLabel,
        colors =
            NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}

@ThemePreviews
@Composable
fun SiniNavigationBarPreview() {
    val items = listOf("Home", "Saved", "Profile")
    val icons = listOf(SiniIcons.HomeBorder, SiniIcons.ArchiveBorder, SiniIcons.ProfileBorder)
    val selectedIcons = listOf(SiniIcons.Home, SiniIcons.Archive, SiniIcons.Profile)
    SiniTheme {
        SiniNavigationBar {
            items.forEachIndexed { index, item ->
                SiniNavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(icons[index]),
                            contentDescription = null,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            painter = painterResource(selectedIcons[index]),
                            contentDescription = item,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    },
                    label = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    selectedLabel = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    selected = index == 0,
                    onClick = { },
                )
            }
        }
    }
}
