package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.ApiResult
import br.com.vinilemess.fintrack.common.ApiResult.Failure
import br.com.vinilemess.fintrack.common.ApiResult.Success
import br.com.vinilemess.fintrack.common.ProblemDetail
import br.com.vinilemess.fintrack.transaction.TransactionType.EXPENSE
import br.com.vinilemess.fintrack.transaction.TransactionType.INCOME
import java.math.BigDecimal
import java.time.LocalDate

class TransactionService(private val transactionRepository: TransactionRepository) {

    suspend fun saveTransaction(createTransactionRequest: CreateTransactionRequest): ApiResult<TransactionInfo> =
        Success(transactionRepository.saveTransaction(createTransactionRequest))

    suspend fun findTransactionById(id: Long): ApiResult<TransactionInfo> =
        transactionRepository.findTransactionById(id)?.let { Success(it) } ?: Failure(
            ProblemDetail(
                title = "Resource not Found",
                status = 404,
                detail = "Transaction with id $id not found"
            )
        )

    suspend fun projectBalanceAtDate(date: LocalDate): ApiResult<BalanceInfo> {
        val transactions: List<TransactionInfo> = transactionRepository.findAllTransactionsUntilDate(date)

        return Success(
            BalanceInfo(
                balance = transactions.sumOf {
                    if (it.type == INCOME) it.amount
                    else it.amount.negate()
                },
                incomingAmount = transactions.sumTransactionsOfType(INCOME),
                outgoingAmount = transactions.sumTransactionsOfType(EXPENSE)
            )
        )
    }

    suspend fun findAllTransactionsUntilDate(date: LocalDate): ApiResult<List<TransactionInfo>> =
        Success(transactionRepository.findAllTransactionsUntilDate(date))

    private fun List<TransactionInfo>.sumTransactionsOfType(type: TransactionType): BigDecimal = this.filter { it.type == type }.sumOf { it.amount }
}