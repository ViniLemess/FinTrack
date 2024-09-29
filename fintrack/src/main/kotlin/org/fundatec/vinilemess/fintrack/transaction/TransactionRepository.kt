package org.fundatec.vinilemess.fintrack.transaction

import org.fundatec.vinilemess.fintrack.transaction.contract.Transaction
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

    @Transactional
    fun deleteByIdList(idList: List<String>) {
        mongoTemplate.remove(filterById(idList), Transaction::class.java, TRANSACTIONS_COLLECTION)
    }

    private fun filterById(ids: List<String>): Query =
        Query().addCriteria(Criteria.where("_id").`in`(ids))

    private fun filterByUserSignatureBeforeDate(
        userSignature: String,
        date: LocalDate
    ): Query = filterByUserSignature(userSignature)
        .addCriteria(Criteria.where("date").lte(date))

    private fun filterByUserSignature(userSignature: String): Query = Query()
        .addCriteria(Criteria.where("userSignature").`is`(userSignature))

    private fun sortDescending(): Sort = Sort.by(Sort.Direction.DESC, "date")
}