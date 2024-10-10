package br.com.vinilemess.fintrack.transaction

class TransactionService(private val transactionRepository: TransactionRepository) {

    fun saveTransaction(createTransactionRequest: CreateTransactionRequest): TransactionResponse {
        val newTransaction = createTransactionFromRequest(createTransactionRequest)
        return transactionRepository.saveTransaction(newTransaction).toResponse()
    }

    fun findTransaction(id: String): TransactionResponse? = transactionRepository.findTransaction(id)?.toResponse()

    fun findAllTransactions(): List<TransactionResponse> = transactionRepository.findTransactions().map {
        it.toResponse()
    }
}