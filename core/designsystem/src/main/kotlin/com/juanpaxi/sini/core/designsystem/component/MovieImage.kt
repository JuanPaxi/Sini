package com.juanpaxi.sini.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.juanpaxi.sini.core.common.network.util.tmdbImageUrl
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons
import com.juanpaxi.sini.core.designsystem.theme.SiniTheme

@Composable
fun MovieImage(
    modifier: Modifier = Modifier,
    imagePath: String? = null,
    imageSize: String = "w500",
    contentDescription: String? = null,
    placeholderIconSize: Dp = 32.dp,
    shape: Shape = RectangleShape,
    alignment: Alignment = Alignment.Center,
) {
    Box(
        modifier =
            modifier
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    MaterialTheme.colorScheme.surface,
                                ),
                        ),
                    shape = shape,
                ).border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = shape,
                ).clip(shape),
        contentAlignment = Alignment.Center,
    ) {
        if (imagePath.isNullOrBlank()) {
            MovieImagePlaceholder(
                icon = SiniIcons.ImageBrokenBorder,
                iconSize = placeholderIconSize,
            )
        } else {
            SubcomposeAsyncImage(
                model = tmdbImageUrl(imagePath, imageSize),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                alignment = alignment,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    MovieImagePlaceholder(
                        icon = SiniIcons.SpinnerGap,
                        iconSize = placeholderIconSize,
                    )
                },
                error = {
                    MovieImagePlaceholder(
                        icon = SiniIcons.ImageBrokenBorder,
                        iconSize = placeholderIconSize,
                    )
                },
            )
        }
    }
}

@ThemePreviews
@Composable
fun MovieImagePreview() {
    SiniTheme {
        MovieImage(
            modifier =
                Modifier
                    .width(360.dp)
                    .height(200.dp),
        )
    }
}
