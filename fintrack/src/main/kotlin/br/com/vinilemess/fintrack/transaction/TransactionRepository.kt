package br.com.vinilemess.fintrack.transaction

import java.util.*
import java.util.concurrent.ConcurrentHashMap

class TransactionRepository(private val transactions: MutableMap<String?, Transaction> = ConcurrentHashMap<String?, Transaction>()) {

    fun saveTransaction(transaction: Transaction): Transaction = transaction.generateTransactionWithId().also {
        transactions[it.id] = it
    }

    fun findTransaction(id: String) = transactions[id]

    fun findTransactions(): List<Transaction> = transactions.values.toList()

    private fun Transaction.generateTransactionWithId() = Transaction(
        id = UUID.randomUUID().toString(),
        amount = this.amount,
        description = this.description,
        type = this.type,
        date = this.date
    )
}