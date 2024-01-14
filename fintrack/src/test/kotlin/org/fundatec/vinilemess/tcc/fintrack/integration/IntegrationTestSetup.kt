package org.fundatec.vinilemess.tcc.fintrack.integration

import io.restassured.RestAssured
import org.fundatec.vinilemess.tcc.fintrack.*
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.user.domain.User
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.ActiveProfiles
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
private const val USERS_COLLECTION = "users"
const val TEST_URL_QUERY_PARAM_DATE = "2023-04-13"
const val DATE_QUERY_NAME = "date"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = ["integration"])
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTestSetup {

    @Autowired
    protected lateinit var mongoTemplate: MongoTemplate

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @LocalServerPort
    private lateinit var port: Number

    @BeforeAll
    fun setup() {
        RestAssured.port = port.toInt()
    }

    protected fun insertIncomeTransactions(amountToInsert: Int = 5,
                                           userSignature: String,
                                           date: LocalDate = LocalDate.now()) {
        for (i in 1..amountToInsert) {
            createTransaction(userSignature, TransactionOperation.INCOME, BigDecimal.TEN, date)
        }
    }

    protected fun insertExpenseTransactions(amountToInsert: Int = 5,
                                            userSignature: String,
                                            date: LocalDate = LocalDate.now()) {
        for (i in 1..amountToInsert) {
            createTransaction(userSignature, TransactionOperation.EXPENSE, BigDecimal.TEN.negate(), date)
        }
    }

    private fun createTransaction(
            userSignature: String,
            transactionOperation: TransactionOperation,
            amount: BigDecimal,
            date: LocalDate
    ) {
        mongoTemplate.save(
                Transaction(
                        id = null,
                        userSignature = userSignature,
                        recurrenceId = null,
                        date = date,
                        amount = amount,
                        description = testDescription,
                        transactionOperation = transactionOperation
                ), TRANSACTIONS_COLLECTION
        )
    }

    protected fun insertUser(transactionSignature: String) {
        mongoTemplate.save(User(
                id = null,
                name = "tester",
                email = "tester@tester.com",
                password = "test123",
                transactionSignature = transactionSignature
        ), USERS_COLLECTION)
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