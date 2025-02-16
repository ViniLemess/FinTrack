package br.com.vinilemess.fintrack.transaction


import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDate

class TransactionRepository(private val fintrackDatabase: Database) {

    suspend fun saveTransaction(transaction: CreateTransactionRequest): TransactionInfo {
        return newSuspendedTransaction(db = fintrackDatabase) {
            Transactions.insertReturning {
                it[amount] = transaction.amount
                it[description] = transaction.description
                it[type] = transaction.type
                it[date] = transaction.date
            }.map(::toTransactionInfo).first()
        }
    }

    suspend fun findTransactionById(id: Long): TransactionInfo? {
        return newSuspendedTransaction(db = fintrackDatabase) {
            Transactions.selectAll()
                .where { Transactions.id eq id }
                .map(::toTransactionInfo)
                .firstOrNull()
        }
    }

    suspend fun findAllTransactionsUntilDate(date: LocalDate): List<TransactionInfo> = newSuspendedTransaction(db = fintrackDatabase) {
        Transactions.selectAll()
            .where { Transactions.date lessEq date.atEndOfDay() }
            .map(::toTransactionInfo)
            .toList()
    }

    private fun toTransactionInfo(result: ResultRow): TransactionInfo = TransactionInfo(
        id = result[Transactions.id],
        amount = result[Transactions.amount],
        type = result[Transactions.type],
        description = result[Transactions.description],
        date = result[Transactions.date]
    )

    private fun LocalDate.atEndOfDay() = this.atTime(23, 59, 59)
}