package org.fundatec.vinilemess.tcc.fintrack.user.repository

import org.fundatec.vinilemess.tcc.fintrack.user.domain.User
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

private const val USERS_COLLECTION = "users"

@Repository
class UserRepository(private val mongoTemplate: MongoTemplate) {

    fun save(user: User): User = mongoTemplate.save(user, USERS_COLLECTION)

    fun findByCredentials(email: String, password: String): User? =
        mongoTemplate.findOne(filterByCredentials(email, password), User::class.java)

    fun findByEmail(email: String): User? =
        mongoTemplate.findOne(filterByEmail(email), User::class.java)

    fun existsSignature(userSignature: String): Boolean =
        mongoTemplate.exists(filterByTransactionSignature(userSignature), User::class.java)

    private fun filterByTransactionSignature(userSignature: String) =
        Query(Criteria.where("transactionSignature").`is`(userSignature))

    private fun filterByCredentials(email: String, password: String) =
        filterByEmail(email).addCriteria(Criteria.where("password").`is`(password))

    private fun filterByEmail(email: String): Query =
        Query().addCriteria(Criteria.where("email").`is`(email))
}