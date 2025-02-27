package org.fundatec.vinilemess.fintrack.integration

import io.jsonwebtoken.Jwts
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.fundatec.vinilemess.fintrack.data.factory.createExpenseTransactionForSignature
import org.fundatec.vinilemess.fintrack.data.factory.createIncomeTransactionForSignature
import org.fundatec.vinilemess.fintrack.security.contract.request.LoginRequest
import org.fundatec.vinilemess.fintrack.security.contract.response.LoginResponse
import org.fundatec.vinilemess.fintrack.transaction.contract.Transaction
import org.fundatec.vinilemess.fintrack.user.contract.Role
import org.fundatec.vinilemess.fintrack.user.contract.User
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
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TRANSACTIONS_COLLECTION = "transactions"
private const val USERS_COLLECTION = "users"
const val DATE_QUERY_NAME = "date"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = ["test"])
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EndToEndTestSetup {

    @Autowired
    protected lateinit var mongoTemplate: MongoTemplate

    @LocalServerPort
    private lateinit var port: Number

    @BeforeAll
    fun setup() {
        RestAssured.port = port.toInt()
    }

    protected fun insertIncomeTransactions(
        amountToInsert: Int = 5,
        userSignature: String,
        date: LocalDate = LocalDate.now()
    ) {
        for (i in 1..amountToInsert) {
            saveTransaction(createIncomeTransactionForSignature(userSignature, date))
        }
    }

    protected fun insertExpenseTransactions(
        amountToInsert: Int = 5,
        userSignature: String,
        date: LocalDate = LocalDate.now()
    ) {
        for (i in 1..amountToInsert) {
            saveTransaction(createExpenseTransactionForSignature(userSignature, date))
        }
    }

    private fun saveTransaction(transaction: Transaction) {
        mongoTemplate.save(transaction, TRANSACTIONS_COLLECTION)
    }

    protected fun insertUser(transactionSignature: String) {
        mongoTemplate.save(
            User(
                id = null,
                name = "tester",
                email = "tester@tester.com",
                password = "test123",
                transactionSignature = transactionSignature,
                role = Role.USER,
                tokens = listOf()
            ), USERS_COLLECTION
        )
    }

    protected fun clearTransactions() {
        mongoTemplate.findAllAndRemove(Query(), Transaction::class.java)
    }

    protected fun findAllTransactions(): List<Transaction> {
        return mongoTemplate.findAll(Transaction::class.java)
    }

    protected fun getTodayDateAsString(): String {
        return LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    }

    protected fun authenticatedRequestSpec(): RequestSpecification {
        val loginResponse: LoginResponse = RestAssured.given()
            .body(LoginRequest("johndoe@example.com", "password123"))
            .contentType(ContentType.JSON)
            .`when`()
            .post("/login")
            .thenReturn()
            .`as`(LoginResponse::class.java)

        return RestAssured.given()
            .headers("Authorization", "Bearer ${loginResponse.token}")
    }

    companion object {
        private val mongoDBContainer: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:latest"))

        @JvmStatic
        @DynamicPropertySource
        fun dynamicApplicationProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri") {
                mongoDBContainer.replicaSetUrl
            }

            registry.add("jwt.generated-secret") {
                val secureKey = Jwts.SIG.HS512.key().build().encoded
                val base64Key = java.util.Base64.getEncoder().encodeToString(secureKey)
                base64Key
            }
        }

        init {
            mongoDBContainer.start()
        }
    }
}