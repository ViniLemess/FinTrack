package org.fundatec.vinilemess.tcc.fintrackapp.usecase

import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrackapp.repository.TransactionRepository


class TransactionUsecase {
    private val repository: TransactionRepository by lazy {
        TransactionRepository()
    }

    suspend fun saveTransaction(transaction: TransactionRequest) {
        repository.saveTransaction(transaction)
    }

    suspend fun findTransactions(userSignature: String): List<Transaction> {
        return repository.findTransactions(userSignature)
    }
}