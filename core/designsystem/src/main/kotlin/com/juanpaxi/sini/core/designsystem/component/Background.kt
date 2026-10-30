package com.juanpaxi.sini.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juanpaxi.sini.core.designsystem.theme.SiniTheme

@Composable
fun SiniBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        content()
    }
}

@Preview
@Composable
fun BackgroundDark() {
    SiniTheme(darkTheme = true) {
        SiniBackground(Modifier.size(100.dp)) {}
    }
}

@Preview
@Composable
fun BackgroundLight() {
    SiniTheme(darkTheme = false) {
        SiniBackground(Modifier.size(100.dp)) {}
    }
}
