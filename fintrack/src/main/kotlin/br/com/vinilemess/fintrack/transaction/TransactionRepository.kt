package br.com.vinilemess.fintrack.transaction

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.bson.BsonValue

class TransactionRepository(private val mongoDatabase: MongoDatabase) {
    private val transactions = this.mongoDatabase.getCollection<Transaction>("transactions")

    suspend fun saveTransaction(transaction: Transaction): Transaction? {
        return transactions.insertOne(transaction).insertedId?.let { findTransactionById(it) }
    }

    suspend fun findTransactionsBySignature(transactionSignature: String): List<Transaction> =
        transactions.find(eq("transactionSignature", transactionSignature)).toList()

    suspend fun findTransactionById(id: BsonValue): Transaction =
        transactions.find(eq("_id", id)).first()
}