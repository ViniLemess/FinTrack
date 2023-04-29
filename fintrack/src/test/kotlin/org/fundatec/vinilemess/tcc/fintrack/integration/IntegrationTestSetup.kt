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
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TRANSACTIONS_COLLECTION = "transactions"
const val TEST_URL_QUERY_PARAM_DATE = "2023-04-13"
const val DATE_QUERY_NAME = "date"


@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTestSetup {

    @Autowired
    protected lateinit var mongoTemplate: MongoTemplate

    @Autowired
    protected lateinit var mockMvc: MockMvc

    protected fun insertTransactions(
        amountToInsert: Int = 5,
        negativeAmount: BigDecimal? = null,
    ) {
        val transactionOperation =
            if (negativeAmount != null) TransactionOperation.EXPENSE else TransactionOperation.INCOME
        for (i in 1..amountToInsert) {
            mongoTemplate.save(
                Transaction(
                    id = null,
                    userSignature = testUserSignature,
                    recurrenceId = null,
                    date = testDate,
                    amount = negativeAmount ?: testPositiveAmount,
                    description = testDescription,
                    transactionOperation = transactionOperation
                ), TRANSACTIONS_COLLECTION
            )
        }
    }

    protected fun clearTransactions() {
        mongoTemplate.findAllAndRemove(Query(), Transaction::class.java)
    }

    protected fun getTodayDateAsString(): String {
        return LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    }

    companion object {
        private val mongoDBContainer: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:latest"))

        @JvmStatic
        @DynamicPropertySource
        fun mongoDbProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri") {
                mongoDBContainer.replicaSetUrl
            }
        }

        init {
            mongoDBContainer.start()
        }
    }
}