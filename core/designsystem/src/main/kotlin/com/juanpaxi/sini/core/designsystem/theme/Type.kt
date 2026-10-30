package com.juanpaxi.sini.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.juanpaxi.sini.core.designsystem.R

val Urbanist =
    FontFamily(
        Font(
            resId = R.font.urbanist_regular,
            weight = FontWeight.Normal,
        ),
        Font(
            resId = R.font.urbanist_medium,
            weight = FontWeight.Medium,
        ),
        Font(
            resId = R.font.urbanist_semibold,
            weight = FontWeight.SemiBold,
        ),
        Font(
            resId = R.font.urbanist_bold,
            weight = FontWeight.Bold,
        ),
    )

val SiniTypography =
    Typography(
        displayLarge =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = (-0.64).sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = (-0.36).sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = (-0.22).sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 22.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 26.sp,
                letterSpacing = (-0.16).sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 22.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.12.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.14.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.24.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = Urbanist,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                letterSpacing = 0.33.sp,
            ),
    )
