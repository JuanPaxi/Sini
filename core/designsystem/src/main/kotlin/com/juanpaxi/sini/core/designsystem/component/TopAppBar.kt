package com.juanpaxi.sini.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons
import com.juanpaxi.sini.core.designsystem.theme.SiniTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiniTopAppBar(
    modifier: Modifier = Modifier,
    @DrawableRes navigationIcon: Int,
    @DrawableRes actionIcon: Int? = null,
    navigationIconTint: Color = Color.Unspecified,
    navigationIconContentDescription: String,
    actionIconContentDescription: String? = null,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    painter = painterResource(navigationIcon),
                    contentDescription = navigationIconContentDescription,
                    modifier = Modifier.size(28.dp),
                    tint = navigationIconTint,
                )
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        painter = painterResource(actionIcon),
                        contentDescription = actionIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
        modifier =
            modifier
                .padding(horizontal = 12.dp),
    )
}

@ThemePreviews
@Composable
private fun SiniTopAppBarPreview() {
    SiniTheme {
        SiniTopAppBar(
            navigationIcon = SiniIcons.Sini,
            actionIcon = SiniIcons.Notification,
            navigationIconContentDescription = "Back",
            actionIconContentDescription = null,
        )
    }
}
