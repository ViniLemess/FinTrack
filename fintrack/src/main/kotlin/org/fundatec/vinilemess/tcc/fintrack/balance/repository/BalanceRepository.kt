package org.fundatec.vinilemess.tcc.fintrack.balance.repository

import org.fundatec.vinilemess.tcc.fintrack.balance.domain.Balance
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class BalanceRepository(private val mongoTemplate: MongoTemplate) {

    @Transactional
    fun upsert(balance: Balance) {
        val query = Query()
                .addCriteria(Criteria.where("userSignature").`is`(balance.userSignature))
        val update = Update()
                .set("userSignature", balance.userSignature)
                .set("amount", balance.amount)
                .set("date", balance.date)

        mongoTemplate.upsert(query, update, Balance::class.java)
    }
}