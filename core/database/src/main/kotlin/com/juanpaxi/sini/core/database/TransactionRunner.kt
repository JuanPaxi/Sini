package com.juanpaxi.sini.core.database

interface TransactionRunner {
    suspend fun <T> run(block: suspend () -> T): T
}
