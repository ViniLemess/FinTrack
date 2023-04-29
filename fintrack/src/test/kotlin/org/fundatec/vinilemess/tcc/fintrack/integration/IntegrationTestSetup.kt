package org.fundatec.vinilemess.tcc.fintrack.integration

import org.fundatec.vinilemess.tcc.fintrack.testDate
import org.fundatec.vinilemess.tcc.fintrack.testDescription
import org.fundatec.vinilemess.tcc.fintrack.testPositiveAmount
import org.fundatec.vinilemess.tcc.fintrack.testUserSignature
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

private const val TRANSACTIONS_COLLECTION = "transactions"

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTestSetup {
    private final val mongoDBContainer: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:latest"))

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    init {
        mongoDBContainer.start()
    }

    fun insertTransactions() {
        for (i in 1..5) {
            mongoTemplate.save(
                Transaction(
                    id = null,
                    userSignature = testUserSignature,
                    recurrenceId = null,
                    date = testDate,
                    amount = testPositiveAmount,
                    description = testDescription,
                    transactionOperation = TransactionOperation.INCOME
                ), TRANSACTIONS_COLLECTION
            )
        }
    }

    fun clearTransactions() {
        mongoTemplate.remove(Any(), TRANSACTIONS_COLLECTION)
    }
}