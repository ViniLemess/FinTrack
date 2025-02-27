package org.fundatec.vinilemess.fintrack.transaction

import org.fundatec.vinilemess.fintrack.transaction.contract.Transaction
import org.fundatec.vinilemess.fintrack.transaction.contract.TransactionResponse
import org.fundatec.vinilemess.fintrack.transaction.contract.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.fintrack.transaction.contract.request.TransactionRequest
import org.fundatec.vinilemess.fintrack.transaction.contract.toResponse
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class TransactionService(private val transactionRepository: TransactionRepository) {

    fun transact(transactionRequest: TransactionRequest, userSignature: String) {
        transactionRepository.save(transactionRequest.buildTransaction(userSignature))
    }

    fun transactRecurrence(recurrentTransaction: RecurrentTransactionRequest, userSignature: String) {
        transactionRepository.saveAll(recurrentTransaction.buildTransactions(userSignature))
    }

    fun listTransactionsBeforeDateByUserSignature(userSignature: String, date: LocalDate): List<TransactionResponse> {
        return transactionRepository.findTransactionsBeforeDateByUserSignature(userSignature, date)
            .map {
                it.toResponse()
            }
    }

    fun deleteTransactionsByIdList(idList: List<String>) {
        transactionRepository.deleteByIdList(idList)
    }

    private fun RecurrentTransactionRequest.buildTransactions(userSignature: String): List<Transaction> {
        var date = inititalDate
        val recurrenceId = UUID.randomUUID().toString()
        return (1..recurrenceCount).map {
            val transaction = buildTransactionForDate(userSignature, date, recurrenceId)
            date = recurrence.calculateDateByFrequency(date, frequency)
            transaction
        }
    }

    private fun TransactionRequest.buildTransaction(userSignature: String) = Transaction(
        id = null,
        userSignature = userSignature,
        recurrenceId = null,
        amount = this.getAmount(),
        date = date,
        description = description,
        transactionOperation = operation
    )

    private fun RecurrentTransactionRequest.buildTransactionForDate(
        userSignature: String,
        date: LocalDate,
        recurrenceId: String
    ) =
        Transaction(
            id = null,
            userSignature = userSignature,
            recurrenceId = recurrenceId,
            amount = this.getAmount(),
            date = date,
            description = description,
            transactionOperation = operation
        )
}