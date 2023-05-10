package org.fundatec.vinilemess.tcc.fintrackapp.repository

import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.TransactionDataSource
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrackapp.toModel

class TransactionRepository {
    private val dataSource: TransactionDataSource by lazy {
        TransactionDataSource()
    }

    suspend fun saveTransaction(transaction: TransactionRequest) {
        dataSource.registerTransaction(transaction)
    }

    suspend fun findTransactions(): List<Transaction> {
        return dataSource.findTransactions().map { it.toModel() }
    }
}