package org.fundatec.vinilemess.tcc.personalbank.transaction.repository

import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.Transaction
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository: MongoRepository<Transaction, String> {
}