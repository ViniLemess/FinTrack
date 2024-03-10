package org.fundatec.vinilemess.fintrack.security.repository

import org.fundatec.vinilemess.fintrack.security.Token
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class TokenRepository(private val mongoTemplate: MongoTemplate) {

    fun findToken(token: String): Token? {
        return mongoTemplate.findOne(Query(Criteria.where("token").`is`(token)), Token::class.java)
    }

    fun findValidTokensByUser(userId: String): Collection<Token> {
        return mongoTemplate.find(
            Query(
                Criteria.where("user.id").`is`(userId)
                    .and("isExpired").`is`(false)
                    .and("isRevoked").`is`(false)
            ),
            Token::class.java
        )
    }

    fun upsert(token: Token) {
        mongoTemplate.save(token)
    }

    fun saveAll(userTokens: Collection<Token>) {
        mongoTemplate.insertAll(userTokens)
    }
}