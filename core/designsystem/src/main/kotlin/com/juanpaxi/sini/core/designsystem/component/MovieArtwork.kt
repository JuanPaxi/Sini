package com.juanpaxi.sini.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juanpaxi.sini.core.designsystem.theme.SiniTheme
import kotlinx.datetime.LocalDate

@Composable
fun MovieArtwork(
    modifier: Modifier = Modifier,
    backdropPath: String? = null,
    posterPath: String? = null,
    title: String,
    isFavorite: Boolean,
    voteAverage: Double,
    runtime: Int?,
    releaseDate: LocalDate?,
    toggleFavorite: () -> Unit,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        MovieImage(
            imagePath = backdropPath,
            modifier = Modifier.fillMaxSize(),
            placeholderIconSize = 64.dp,
        )
        ContentInformation(
            modifier =
                Modifier
                    .align(Alignment.BottomStart),
            posterPath = posterPath,
            title = title,
            voteAverage = voteAverage,
            runtime = runtime,
            releaseDate = releaseDate,
        )
        MovieFavoriteButton(
            isFavorite = isFavorite,
            onClick = toggleFavorite,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(vertical = 32.dp, horizontal = 16.dp),
        )
        BackButton(
            onClick = onBackClick,
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(vertical = 32.dp, horizontal = 16.dp),
        )
    }
}

@ThemePreviews
@Composable
fun MovieArtworkPreview() {
    SiniTheme {
        MovieArtwork(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(400.dp),
            isFavorite = true,
            toggleFavorite = {},
            onBackClick = {},
            title = "Coming",
            voteAverage = 8.98,
            runtime = 205,
            releaseDate = LocalDate.parse("2026-07-15"),
        )
    }
}
