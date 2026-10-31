package com.juanpaxi.sini.core.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons

@Composable
fun MovieFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter =
                painterResource(
                    if (isFavorite) SiniIcons.Heart else SiniIcons.HeartBorder,
                ),
            contentDescription = if (isFavorite) "Remove Favourite" else "Add Favourite",
            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(SiniIcons.ArrowLeftBorder),
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
