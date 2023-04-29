package org.fundatec.vinilemess.tcc.fintrack.balance.service

import org.fundatec.vinilemess.tcc.fintrack.balance.domain.BalanceResponse
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.TransactionResponse
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class BalanceService(private val transactionService: TransactionService) {

    fun calculateBalanceForDate(userSignature: String, date: LocalDate): BalanceResponse {
        val transactions = transactionService.listTransactionsBeforeDateByUserSignature(userSignature, date)
        val balance = extractAmountSum(transactions)
        return BalanceResponse(balance, date)
    }

    private fun extractAmountSum(transactions: List<TransactionResponse>): BigDecimal {
        if (transactions.isEmpty()) return BigDecimal.ZERO
        return transactions.map { transaction -> transaction.amount }.reduce(BigDecimal::add)
    }
}