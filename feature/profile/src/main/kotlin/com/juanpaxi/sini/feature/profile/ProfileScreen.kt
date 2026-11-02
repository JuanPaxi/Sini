package com.juanpaxi.sini.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juanpaxi.sini.feature.profile.component.AboutSection
import com.juanpaxi.sini.feature.profile.component.LanguageSection
import com.juanpaxi.sini.feature.profile.component.ProfileHeader
import com.juanpaxi.sini.feature.profile.component.ThemeSection

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val darkThemeConfig by viewModel.darkThemeConfig.collectAsStateWithLifecycle()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        ProfileHeader(email = "https://github.com/JuanPaxi")
        LanguageSection()
        ThemeSection(
            selectedTheme = darkThemeConfig,
            onThemeSelected = viewModel::setDarkTheme,
        )
        AboutSection(
            onGithubClick = {},
            onContactClick = {},
        )
    }
}
