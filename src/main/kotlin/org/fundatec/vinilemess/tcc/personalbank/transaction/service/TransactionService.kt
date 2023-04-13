package org.fundatec.vinilemess.tcc.personalbank.transaction.service

import org.fundatec.vinilemess.tcc.personalbank.balance.Balance
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.request.TransactionExpense
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.request.TransactionIncome
import org.fundatec.vinilemess.tcc.personalbank.transaction.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class TransactionService(private val transactionRepository: TransactionRepository) {

    fun transactIncome(transactionIncome: TransactionIncome, userIdentifier: String) {
        transactionRepository.save(transactionIncome.buildTransaction(userIdentifier))
    }

    fun transactExpense(transactionExpense: TransactionExpense, userIdentifier: String) {
        transactionRepository.save(transactionExpense.buildTransaction(userIdentifier))
    }

    fun calculateBalanceForDate(userIdentifier: String, date: LocalDate): Balance {
        val transactions = getTransactionsBeforeDateByUserIdentifier(userIdentifier, date)
        val balance = extractAmountSum(transactions)
        return Balance(balance, date)
    }

    private fun extractAmountSum(transactions: List<Transaction>) =
        transactions.map { transaction ->
            transaction.amount
        }.reduce(BigDecimal::add)

    private fun getTransactionsBeforeDateByUserIdentifier(userIdentifier: String, date: LocalDate): List<Transaction> {
        return transactionRepository.findTransactionsBeforeDateByUserIdentifier(userIdentifier, date)
    }

    private fun TransactionIncome.buildTransaction(userIdentifier: String) = Transaction(
        id = null,
        userIdentifier = userIdentifier,
        amount = this.amount,
        date = this.date
    )


    private fun TransactionExpense.buildTransaction(userIdentifier: String) = Transaction(
        id = null,
        userIdentifier = userIdentifier,
        amount = this.amount,
        date = this.date
    )
}