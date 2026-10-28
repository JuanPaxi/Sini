package com.juanpaxi.sini.core.data.testdoubles

import com.juanpaxi.sini.core.data.Synchronizer

class TestSynchronizer : Synchronizer {
    var totalPages = 10
    var requestedPage: Int? = null
    var exception: Exception? = null

    override suspend fun sync(page: Int): Int {
        requestedPage = page
        exception?.let { throw it }
        return totalPages
    }
}
