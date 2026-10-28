package com.juanpaxi.sini.core.database

import androidx.room3.withWriteTransaction
import javax.inject.Inject

class RoomTransactionRunner
    @Inject
    constructor(
        private val database: SiniDatabase,
    ) : TransactionRunner {
        override suspend fun <T> run(block: suspend () -> T): T =
            database.withWriteTransaction {
                block()
            }
    }
