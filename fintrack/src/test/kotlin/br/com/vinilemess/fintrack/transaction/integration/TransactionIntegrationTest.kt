package br.com.vinilemess.fintrack.transaction.integration

import br.com.vinilemess.fintrack.ProblemDetail
import br.com.vinilemess.fintrack.transaction.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Test
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import java.math.BigDecimal.TEN
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private const val TRANSACTION_PATH = "/transaction"

class TransactionIntegrationTest {

    private val deserializer = Json(Json.Default) {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    @Test
    fun `Should return created transaction with 201 status when a transaction is sucessfully saved`() =
        testApplication {
            val createTransactionRequest = defaultCreateTransactionRequest(type = TransactionType.INVESTMENT)

            val response = client.post(TRANSACTION_PATH) {
                setBody(deserializer.encodeToString(serializer(), createTransactionRequest))
                contentType(ContentType.Application.Json)
            }

            val deserializedTransaction = deserializer.decodeFromString<TransactionResponse>(response.body())

            assertEquals(HttpStatusCode.Created, response.status)
            assertNotNull(deserializedTransaction.id, "Transaction ID should not be null")
            assertEquals(createTransactionRequest.description, deserializedTransaction.description)
            assertEquals(createTransactionRequest.amount, deserializedTransaction.amount)
            assertEquals(createTransactionRequest.type, deserializedTransaction.type)
            assertEquals(createTransactionRequest.date, deserializedTransaction.date)
        }

    @Test
    fun `Should return all saved transaction with 200 status when requested`() = testApplication {
        application {
            val transactionRepository: TransactionRepository by closestDI().instance<TransactionRepository>()
            transactionRepository.saveTransaction(defaultTransaction())
            transactionRepository.saveTransaction(defaultTransaction())
            transactionRepository.saveTransaction(defaultTransaction())
        }

        val response = client.get(TRANSACTION_PATH)

        val deserializedTransactions = deserializer.decodeFromString<List<TransactionResponse>>(response.body())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(3, deserializedTransactions.size)
        deserializedTransactions.forEach { deserializedTransaction ->
            assertNotNull(deserializedTransaction.id, "Transaction ID should not be null")
            assertEquals("test transaction", deserializedTransaction.description)
            assertEquals(TEN.toString(), deserializedTransaction.amount)
            assertEquals(TransactionType.INCOME, deserializedTransaction.type)
            assertEquals(LocalDateTime.MAX.toString(), deserializedTransaction.date)
        }
    }

    @Test
    fun `Should return problem details with 404 status when no transaction is found for provided id`() =
        testApplication {
            val expectedProblemDetails = ProblemDetail(
                title = "Resource not found",
                status = 404,
                detail = "Transaction not found",
                instance = TRANSACTION_PATH
            )

            val response = client.get("$TRANSACTION_PATH/1")

            val deserializedProblemDetails = deserializer.decodeFromString<ProblemDetail>(response.body())

            assertEquals(HttpStatusCode.NotFound, response.status)
            assertEquals(expectedProblemDetails, deserializedProblemDetails)
        }

    @Test
    fun `Should return requested transaction for id with 200 status`() = testApplication {
        val expectedTransaction = defaultTransactionResponse(id = "1")

        setupPreSavedTransactionsEnv()

        val response = client.get("$TRANSACTION_PATH/1")

        val deserializedProblemDetails = deserializer.decodeFromString<TransactionResponse>(response.body())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expectedTransaction, deserializedProblemDetails)
    }

    private fun ApplicationTestBuilder.setupPreSavedTransactionsEnv() {
        environment {
            config = ApplicationConfig("application-test.yaml")
        }
        application {
            di {
                import(module = DI.Module("testAppModules") {
                    bind<TransactionRepository>() with singleton {
                        TransactionRepository(
                            mutableMapOf(
                                "1" to defaultTransaction(
                                    id = "1"
                                )
                            )
                        )
                    }
                    bind<TransactionService>() with singleton { TransactionService(instance()) }
                    bind<TransactionController>() with singleton { TransactionController(instance()) }
                })
            }
            routing {
                val transactionController: TransactionController by closestDI().instance<TransactionController>()
                transactionController.registerRoutes(this)
            }
        }
    }
}