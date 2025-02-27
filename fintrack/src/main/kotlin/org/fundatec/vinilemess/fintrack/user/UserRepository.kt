package org.fundatec.vinilemess.fintrack.user

import org.fundatec.vinilemess.fintrack.user.contract.User
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

private const val USERS_COLLECTION = "users"

@Repository
class UserRepository(private val mongoTemplate: MongoTemplate) {

    fun save(user: User): User = mongoTemplate.save(user, USERS_COLLECTION)

    fun findByEmail(email: String): User? =
        mongoTemplate.findOne(filterByEmail(email), User::class.java)

    fun existsSignature(userSignature: String): Boolean =
        mongoTemplate.exists(filterByTransactionSignature(userSignature), User::class.java)

    private fun filterByTransactionSignature(userSignature: String) =
        Query(Criteria.where("transactionSignature").`is`(userSignature))

    private fun filterByEmail(email: String): Query =
        Query().addCriteria(Criteria.where("email").`is`(email))
}