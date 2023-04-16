package org.fundatec.vinilemess.tcc.fintrack.balance.repository

import org.fundatec.vinilemess.tcc.fintrack.balance.domain.Balance
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class BalanceRepository(private val mongoTemplate: MongoTemplate) {

    fun upsert(balance: Balance) {
        mongoTemplate.upsert(filterByUserSignatureQuery(balance.userSignature), balanceUpdate(balance), Balance::class.java)
    }

    fun findBalanceByUserSignature(userSignature: String): Balance? {
        return mongoTemplate.findOne(filterByUserSignatureQuery(userSignature), Balance::class.java)
    }

    private fun filterByUserSignatureQuery(userSignature: String) = Query()
        .addCriteria(Criteria.where("userSignature").`is`(userSignature))

    private fun balanceUpdate(balance: Balance) = Update()
        .set("userSignature", balance.userSignature)
        .set("amount", balance.amount)
        .set("date", balance.date)
}