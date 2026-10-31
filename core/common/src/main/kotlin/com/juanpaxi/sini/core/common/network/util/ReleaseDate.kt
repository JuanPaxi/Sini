package com.juanpaxi.sini.core.common.network.util

import kotlinx.datetime.LocalDate

private val englishMonths =
    listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
    )

fun LocalDate.toReleaseDateString(): String = "${englishMonths[month.ordinal]} $day, $year"
