package org.fundatec.vinilemess.tcc.fintrack.transaction.service

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class TransactionService(private val transactionRepository: TransactionRepository) {

    fun transact(transactionRequest: TransactionRequest, userSignature: String) {
        transactionRepository.save(transactionRequest.buildTransaction(userSignature))
    }

    fun transactRecurrence(recurrentTransaction: RecurrentTransactionRequest, userSignature: String) {
        transactionRepository.saveAll(recurrentTransaction.buildTransactions(userSignature))
    }

    fun getTransactionsBeforeDateByUserSignature(userSignature: String, date: LocalDate): List<Transaction> {
        return transactionRepository.findTransactionsBeforeDateByUserSignature(userSignature, date)
    }

    private fun TransactionRequest.buildTransaction(userSignature: String) = Transaction(
        id = null,
        userSignature = userSignature,
        amount = amount,
        date = date,
        description = description,
        transactionOperation = operation
    )

    private fun RecurrentTransactionRequest.buildTransactions(userSignature: String): List<Transaction> {
        var date = inititalDate
        return (1..recurrenceCount).map {
            val transaction = Transaction(
                id = null,
                userSignature = userSignature,
                amount = amount,
                date = date,
                description = description,
                transactionOperation = operation
            )
            date = recurrence.calculateDateByFrequency(date, frequency)
            transaction
        }
    }
}