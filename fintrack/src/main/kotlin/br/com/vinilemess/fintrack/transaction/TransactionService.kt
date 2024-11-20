package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.ApiResult
import br.com.vinilemess.fintrack.common.ApiResult.Failure
import br.com.vinilemess.fintrack.common.ApiResult.Success
import br.com.vinilemess.fintrack.common.ProblemDetail

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
}