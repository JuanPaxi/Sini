package com.juanpaxi.sini.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.juanpaxi.sini.core.common.network.util.toReleaseDateString
import kotlinx.datetime.LocalDate

@Composable
fun MovieReleaseDate(
    releaseDate: LocalDate?,
    modifier: Modifier = Modifier,
) {
    if (releaseDate != null) {
        Text(
            text = releaseDate.toReleaseDateString(),
            modifier = modifier,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White.copy(alpha = 0.75f),
        )
    }
}
