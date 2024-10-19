package br.com.vinilemess.fintrack.transaction

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.bson.BsonObjectId
import org.bson.BsonValue
import org.bson.types.ObjectId

class TransactionRepository(private val mongoDatabase: MongoDatabase) {
    private val transactions = this.mongoDatabase.getCollection<Transaction>("transactions")

    suspend fun saveTransaction(transaction: Transaction): Transaction? {
        return transactions.insertOne(transaction).insertedId?.let { transaction.copyWithId(it.asObjectId()) }
    }

    suspend fun findTransactionsBySignature(transactionSignature: String): List<Transaction> =
        transactions.find(eq("transactionSignature", transactionSignature)).toList()
}