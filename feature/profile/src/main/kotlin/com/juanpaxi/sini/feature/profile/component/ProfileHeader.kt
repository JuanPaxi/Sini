package com.juanpaxi.sini.feature.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.juanpaxi.sini.core.designsystem.component.ThemePreviews
import com.juanpaxi.sini.core.designsystem.theme.SiniTheme
import com.juanpaxi.sini.feature.profile.R

@Composable
fun ProfileHeader(
    email: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .blur(15.dp),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape),
            )
            Text(
                text = email,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.78f),
            )
            Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
        }
    }
}

@ThemePreviews
@Composable
fun ProfileHeaderPreview() {
    SiniTheme {
        ProfileHeader(
            email = "https://github.com/JuanPaxi",
        )
    }
}
