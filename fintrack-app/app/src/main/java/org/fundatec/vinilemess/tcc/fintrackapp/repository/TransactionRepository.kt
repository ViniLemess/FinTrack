package org.fundatec.vinilemess.tcc.fintrackapp.repository

import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.TransactionDataSource
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrackapp.toModel

class TransactionRepository {
    private val dataSource: TransactionDataSource by lazy {
        TransactionDataSource()
    }

    suspend fun saveTransaction(transaction: TransactionRequest, userSignature: String) {
        dataSource.registerTransaction(transaction, userSignature)
    }

    suspend fun findTransactions(userSignature: String, date: String?): List<Transaction> {
        return dataSource.findTransactions(userSignature, date).map { it.toModel() }
    }
}