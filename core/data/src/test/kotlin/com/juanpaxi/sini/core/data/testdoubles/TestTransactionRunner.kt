package com.juanpaxi.sini.core.data.testdoubles

import com.juanpaxi.sini.core.database.TransactionRunner

class TestTransactionRunner : TransactionRunner {
    override suspend fun <T> run(block: suspend () -> T): T = block()
}
