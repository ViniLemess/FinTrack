package org.fundatec.vinilemess.tcc.personalbank.transaction.repository

import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.Transaction
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

private const val TRANSACTIONS_COLLECTION =  "transactions"

@Repository
class TransactionRepository(private val mongoTemplate: MongoTemplate) {

    fun findTransactionsBeforeDateByUserIdentifier(userIdentifier: String, date: LocalDate): List<Transaction> {
        val query = Query()
            .addCriteria(Criteria.where("userIdentifier").`is`(userIdentifier))
            .addCriteria(Criteria.where("date").lt(date))

        return mongoTemplate.find(query, Transaction::class.java, TRANSACTIONS_COLLECTION)
    }

    fun save(transaction: Transaction) {
        mongoTemplate.save(transaction, TRANSACTIONS_COLLECTION)
    }
}