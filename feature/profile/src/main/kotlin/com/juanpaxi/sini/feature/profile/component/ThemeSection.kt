package com.juanpaxi.sini.feature.profile.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.juanpaxi.sini.core.designsystem.component.ThemePreviews
import com.juanpaxi.sini.core.designsystem.icon.SiniIcons
import com.juanpaxi.sini.core.designsystem.theme.SiniTheme
import com.juanpaxi.sini.core.model.data.DarkThemeConfig

private data class ThemeOption(
    val theme: DarkThemeConfig,
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
)

@Composable
fun ThemeSection(
    selectedTheme: DarkThemeConfig,
    onThemeSelected: (DarkThemeConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options =
        listOf(
            ThemeOption(
                theme = DarkThemeConfig.LIGHT,
                label = "Light",
                selectedIcon = SiniIcons.Sun,
                unselectedIcon = SiniIcons.SunBorder,
            ),
            ThemeOption(
                theme = DarkThemeConfig.DARK,
                label = "Dark",
                selectedIcon = SiniIcons.Moon,
                unselectedIcon = SiniIcons.MoonBorder,
            ),
            ThemeOption(
                theme = DarkThemeConfig.FOLLOW_SYSTEM,
                label = "System",
                selectedIcon = SiniIcons.Desktop,
                unselectedIcon = SiniIcons.DesktopBorder,
            ),
        )

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        options.forEach { option ->
            val selected = selectedTheme == option.theme

            val containerColor =
                if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                }

            val contentColor =
                if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }

            Surface(
                modifier =
                    Modifier
                        .weight(1f)
                        .clickable {
                            onThemeSelected(option.theme)
                        },
                shape = RoundedCornerShape(12.dp),
                color = containerColor,
                border =
                    BorderStroke(
                        1.dp,
                        if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter =
                            painterResource(
                                id =
                                    if (selected) {
                                        option.selectedIcon
                                    } else {
                                        option.unselectedIcon
                                    },
                            ),
                        contentDescription = null,
                        tint =
                            if (selected) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                    )

                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.labelLarge,
                        color = contentColor,
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun ThemeSectionPreview() {
    SiniTheme {
        ThemeSection(
            selectedTheme = DarkThemeConfig.FOLLOW_SYSTEM,
            onThemeSelected = {},
        )
    }
}
