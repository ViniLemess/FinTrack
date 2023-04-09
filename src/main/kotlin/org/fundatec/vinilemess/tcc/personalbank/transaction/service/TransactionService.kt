package org.fundatec.vinilemess.tcc.personalbank.transaction.service

import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input.TransactionExpense
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input.TransactionIncome
import org.fundatec.vinilemess.tcc.personalbank.transaction.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class TransactionService(private val transactionRepository: TransactionRepository) {

    fun transactIncome(transactionIncome: TransactionIncome) {
        transactionRepository.save(transactionIncome.buildTransaction())
    }

    fun transactExpense(transactionExpense: TransactionExpense) {
        transactionRepository.save(transactionExpense.buildTransaction())
    }

    private fun TransactionIncome.buildTransaction(): Transaction {
        return Transaction(
            id = null,
            userIdentifier = UUID.randomUUID().toString(),
            amount = BigDecimal.valueOf(this.amount),
            date = this.incomeDate
        )
    }

    private fun TransactionExpense.buildTransaction(): Transaction {
        return Transaction(
            id = null,
            userIdentifier = UUID.randomUUID().toString(),
            amount = BigDecimal.valueOf(this.amount),
            date = this.expenseDate
        )
    }
}