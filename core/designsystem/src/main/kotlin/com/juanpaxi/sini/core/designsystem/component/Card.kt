package com.juanpaxi.sini.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.juanpaxi.sini.core.common.network.util.tmdbImageUrl
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons
import com.juanpaxi.sini.core.designsystem.theme.SiniTheme
import com.juanpaxi.sini.core.model.data.Movie

// @Composable
// fun MovieCard(
//    modifier: Modifier = Modifier,
//    movie: Movie,
//    onMovieDetailClick: (Int) -> Unit,
// ) {
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .clip(RoundedCornerShape(12.dp))
//            .background(MaterialTheme.colorScheme.surfaceVariant)
//            .clickable { onMovieDetailClick(movie.id) }
//            .border(
//                width = 1.dp,
//                color = MaterialTheme.colorScheme.outlineVariant,
//                shape = RoundedCornerShape(12.dp),
//            ),
//    ) {
//        AsyncImage(
//            model = tmdbImageUrl(movie.posterPath, "w500"),
//            contentDescription = movie.title,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxSize(),
//        )
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth()
//                .fillMaxHeight(0.42f)
//                .background(
//                    Brush.verticalGradient(
//                        colors = listOf(
//                            Color.Transparent,
//                            Color(0xFF121315).copy(alpha = 0.82f),
//                        ),
//                    ),
//                ),
//        )
//        VoteAverageBadge(
//            modifier = Modifier.align(Alignment.TopEnd),
//            voteAverage = 7.98
//        )
//    }
// }

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Movie,
    onMovieDetailClick: (Int) -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(0.68f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                    onMovieDetailClick(movie.id)
                }.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp),
                ),
    ) {
        AsyncImage(
            model = tmdbImageUrl(movie.posterPath, "w500"),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(SiniIcons.ImageBorder),
            error = painterResource(SiniIcons.ImageBrokenBorder),
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.88f),
                                ),
                        ),
                    ),
        )
        VoteAverageBadge(
            modifier = Modifier.align(Alignment.TopEnd),
            voteAverage = movie.voteAverage,
        )
        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp,
                        vertical = 12.dp,
                    ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = movie.title,
                color = Color.White.copy(alpha = 0.78f),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@ThemePreviews
@Composable
fun MovieCardPreview() {
    val movie =
        Movie(
            id = 123,
            title = "Resident Evil: Welcome to Raccoon City",
            posterPath = "",
            voteAverage = 7.98,
        )
    SiniTheme {
        MovieCard(
            modifier =
                Modifier
                    .height(270.dp)
                    .width(180.dp),
            movie = movie,
            onMovieDetailClick = {},
        )
    }
}
