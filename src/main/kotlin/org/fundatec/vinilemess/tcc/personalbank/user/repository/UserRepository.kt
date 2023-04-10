package org.fundatec.vinilemess.tcc.personalbank.user.repository

import org.fundatec.vinilemess.tcc.personalbank.user.domain.User
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

private const val USERS_COLLECTION = "users"

@Repository
class UserRepository(private val mongoTemplate: MongoTemplate) {

    fun save(user: User): User {
        return mongoTemplate.save(user, USERS_COLLECTION)
    }
}