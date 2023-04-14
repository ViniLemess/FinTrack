package org.fundatec.vinilemess.tcc.fintrack.transaction.service

import org.fundatec.vinilemess.tcc.fintrack.balance.domain.response.BalanceResponse
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionExpense
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionIncome
import org.fundatec.vinilemess.tcc.fintrack.transaction.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class TransactionService(private val transactionRepository: TransactionRepository) {

    fun transactIncome(transactionIncome: TransactionIncome, userSignature: String) {
        transactionRepository.save(transactionIncome.buildTransaction(userSignature))
    }

    fun transactExpense(transactionExpense: TransactionExpense, userSignature: String) {
        transactionRepository.save(transactionExpense.buildTransaction(userSignature))
    }

    fun calculateBalanceForDate(userSignature: String, date: LocalDate): BalanceResponse {
        val transactions = getTransactionsBeforeDateByUserSignature(userSignature, date)
        val balance = extractAmountSum(transactions)
        return BalanceResponse(balance, date)
    }

    fun extractAmountSum(transactions: List<Transaction>) : BigDecimal {
        if (transactions.isEmpty()) return BigDecimal.ZERO
        return transactions.map { transaction -> transaction.amount }.reduce(BigDecimal::add)
    }


    private fun getTransactionsBeforeDateByUserSignature(userSignature: String, date: LocalDate): List<Transaction> {
        return transactionRepository.findTransactionsBeforeDateByUserSignature(userSignature, date)
    }

    private fun TransactionIncome.buildTransaction(userSignature: String) = Transaction(
            id = null,
            userSignature = userSignature,
            amount = this.amount,
            date = this.date,
            description = this.description,
            transactionOperation = this.operation
    )


    private fun TransactionExpense.buildTransaction(userSignature: String) = Transaction(
            id = null,
            userSignature = userSignature,
            amount = this.amount,
            date = this.date,
            description = this.description,
            transactionOperation = this.operation
    )
}