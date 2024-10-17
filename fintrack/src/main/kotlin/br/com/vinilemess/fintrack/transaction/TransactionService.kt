package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.ApiResult
import br.com.vinilemess.fintrack.common.ApiResult.Failure
import br.com.vinilemess.fintrack.common.ApiResult.Success
import br.com.vinilemess.fintrack.error.ProblemDetail

class TransactionService(private val transactionRepository: TransactionRepository) {

    suspend fun saveTransaction(createTransactionRequest: CreateTransactionRequest): ApiResult<TransactionResponse> {
        val newTransaction = createTransactionFromRequest(createTransactionRequest)
        return transactionRepository.saveTransaction(newTransaction)
            ?.toResponse()
            ?.let { Success(it) }
            ?: Failure(
                ProblemDetail(
                    title = "Internal Error",
                    status = 500,
                    detail = "Something unexpected happened",
                )
            )
    }

    suspend fun findTransactionsBySignature(id: String): Success<List<TransactionResponse>> =
        Success(transactionRepository.findTransactionsBySignature(id).map {
            it.toResponse()
        })
}