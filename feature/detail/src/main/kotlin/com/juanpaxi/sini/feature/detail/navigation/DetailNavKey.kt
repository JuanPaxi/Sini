package com.juanpaxi.sini.feature.detail.navigation

import androidx.navigation3.runtime.NavKey
import com.juanpaxi.sini.core.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class DetailNavKey(
    val movieId: Int,
) : NavKey

fun Navigator.navigateToMovieDetail(movieId: Int) {
    navigate(DetailNavKey(movieId))
}
