package org.fundatec.vinilemess.tcc.fintrack.transaction.repository

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

private const val TRANSACTIONS_COLLECTION = "transactions"

@Repository
class TransactionRepository(private val mongoTemplate: MongoTemplate) {

    @Transactional
    fun save(transaction: Transaction) {
        mongoTemplate.save(transaction, TRANSACTIONS_COLLECTION)
    }

    @Transactional
    fun saveAll(transaction: List<Transaction>) {
        mongoTemplate.insert(transaction, Transaction::class.java)
    }

    fun findTransactionsBeforeDateByUserSignature(userSignature: String, date: LocalDate): List<Transaction> {
        return mongoTemplate.find(
            filterByUserSignatureBeforeDate(userSignature, date).with(sortDescending()),
            Transaction::class.java,
            TRANSACTIONS_COLLECTION
        )
    }

    fun deleteById(id: String) {
        mongoTemplate.remove(filterById(id), Transaction::class.java, TRANSACTIONS_COLLECTION)
    }

    private fun filterById(id: String): Query =
        Query().addCriteria(Criteria.where("id").`is`(id))

    private fun filterByUserSignatureBeforeDate(
        userSignature: String,
        date: LocalDate
    ): Query = filterByUserSignature(userSignature)
        .addCriteria(Criteria.where("date").lte(date))

    private fun filterByUserSignature(userSignature: String): Query = Query()
        .addCriteria(Criteria.where("userSignature").`is`(userSignature))

    private fun sortDescending(): Sort = Sort.by(Sort.Direction.DESC, "date")
}