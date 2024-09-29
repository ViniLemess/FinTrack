package org.fundatec.vinilemess.fintrack.balance

import org.fundatec.vinilemess.fintrack.exception.NotFoundException
import org.fundatec.vinilemess.fintrack.transaction.contract.TransactionResponse
import org.fundatec.vinilemess.fintrack.transaction.TransactionService
import org.fundatec.vinilemess.fintrack.user.UserService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class BalanceService(private val transactionService: TransactionService, private val userService: UserService) {

    fun calculateBalanceForDate(userSignature: String, date: LocalDate): BalanceResponse {
        validateSignatureExistence(userSignature)
        val transactions = transactionService.listTransactionsBeforeDateByUserSignature(userSignature, date)
        val balance = extractAmountSum(transactions)
        return BalanceResponse(balance, date)
    }

    private fun extractAmountSum(transactions: List<TransactionResponse>): BigDecimal {
        if (transactions.isEmpty()) return BigDecimal.ZERO
        return transactions.map { transaction -> transaction.amount }.reduce(BigDecimal::add)
    }

    private fun validateSignatureExistence(userSignature: String) {
        if (userService.existsSignature(userSignature)) {
            return
        }
        throw NotFoundException("The provided user signature does not exist!")
    }
}