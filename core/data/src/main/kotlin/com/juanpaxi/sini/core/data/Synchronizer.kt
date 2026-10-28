package com.juanpaxi.sini.core.data

interface Synchronizer {
    suspend fun sync(page: Int): Int
}
