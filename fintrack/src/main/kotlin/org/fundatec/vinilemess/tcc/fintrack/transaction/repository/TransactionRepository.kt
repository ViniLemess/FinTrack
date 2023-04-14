package org.fundatec.vinilemess.tcc.fintrack.transaction.repository

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

private const val TRANSACTIONS_COLLECTION =  "transactions"

@Repository
class TransactionRepository(private val mongoTemplate: MongoTemplate) {

    fun findTransactionsBeforeDateByUserSignature(userSignature: String, date: LocalDate): List<Transaction> {
        val query = Query()
            .addCriteria(Criteria.where("userSignature").`is`(userSignature))
            .addCriteria(Criteria.where("date").lte(date))

        return mongoTemplate.find(query, Transaction::class.java, TRANSACTIONS_COLLECTION)
    }

    fun save(transaction: Transaction) {
        mongoTemplate.save(transaction, TRANSACTIONS_COLLECTION)
    }
}