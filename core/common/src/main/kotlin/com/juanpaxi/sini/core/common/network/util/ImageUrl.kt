package com.juanpaxi.sini.core.common.network.util

fun tmdbImageUrl(
    posterPath: String?,
    size: String,
): String? =
    posterPath?.let {
        "https://image.tmdb.org/t/p/$size$it"
    }
