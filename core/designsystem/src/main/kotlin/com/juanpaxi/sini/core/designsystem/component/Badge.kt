package com.juanpaxi.sini.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons
import com.juanpaxi.sini.core.designsystem.theme.SiniTheme
import java.util.Locale

@Composable
fun VoteAverageBadge(
    modifier: Modifier = Modifier,
    voteAverage: Double,
) {
    Row(
        modifier =
            modifier
                .padding(6.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                ).clip(CircleShape)
                .background(
                    brush =
                        Brush.horizontalGradient(
                            colors =
                                listOf(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.78f),
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
                                ),
                        ),
                ).border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                    shape = CircleShape,
                ).padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Icon(
            painter = painterResource(SiniIcons.Star),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text =
                String.format(
                    Locale.US,
                    "%.1f",
                    voteAverage,
                ),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@ThemePreviews
@Composable
fun VoteAverageBadgePreview() {
    SiniTheme {
        VoteAverageBadge(voteAverage = 7.498)
    }
}
