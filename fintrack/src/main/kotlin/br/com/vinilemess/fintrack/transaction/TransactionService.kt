package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.error.ErrorResponseException
import br.com.vinilemess.fintrack.error.ProblemDetail
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class TransactionService(private val transactionRepository: TransactionRepository) {

    suspend fun saveTransaction(createTransactionRequest: CreateTransactionRequest): Result<TransactionResponse> {
        val newTransaction = createTransactionFromRequest(createTransactionRequest)
        return transactionRepository.saveTransaction(newTransaction)
            ?.toResponse()
            ?.let { success(it) }
            ?: failure(
                exception = ErrorResponseException(
                    ProblemDetail(
                        title = "Internal Error",
                        status = 500,
                        detail = "Something unexpected happened",
                    )
                )
            )
    }

    suspend fun findTransactionsBySignature(id: String): List<TransactionResponse> =
        transactionRepository.findTransactionsBySignature(id).map {
            it.toResponse()
        }
}