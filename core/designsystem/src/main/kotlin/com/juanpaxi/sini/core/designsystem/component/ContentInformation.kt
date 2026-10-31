package com.juanpaxi.sini.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate

@Composable
fun ContentInformation(
    modifier: Modifier = Modifier,
    posterPath: String?,
    title: String,
    voteAverage: Double,
    runtime: Int?,
    releaseDate: LocalDate?,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MovieImage(
            imagePath = posterPath,
            modifier = Modifier.size(width = 120.dp, height = 180.dp),
            shape = RoundedCornerShape(8.dp),
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White.copy(alpha = 0.78f),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                VoteAverage(
                    voteAverage = voteAverage,
                    iconSize = 18.dp,
                    textStyle = MaterialTheme.typography.titleSmall,
                )

                MovieRuntime(runtime = runtime)
            }
            MovieReleaseDate(
                releaseDate = releaseDate,
            )
        }
    }
}
