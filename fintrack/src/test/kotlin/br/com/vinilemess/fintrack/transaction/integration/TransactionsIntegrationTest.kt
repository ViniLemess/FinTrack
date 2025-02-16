package br.com.vinilemess.fintrack.transaction.integration

import br.com.vinilemess.fintrack.transaction.TransactionType.INCOME
import br.com.vinilemess.fintrack.transaction.Transactions
import br.com.vinilemess.fintrack.transaction.testDate
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentType.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

private const val TRANSACTION_PATH = "/transaction"

class TransactionsIntegrationTest : IntegrationTestSetup() {

    @Test
    fun `Should return created transaction with status 201 when a transaction is successfully recorded`() = setupApplicationTest {
        val expectedCreatedTransactionResult = """
                {
                    "id": 1,
                    "amount": "10.00",
                    "description": "test transaction",
                    "type": "EXPENSE",
                    "date": "$testDate"
                }
        """.trimIndent()

        val response = client.post(TRANSACTION_PATH) {
            setBody(
                """
                {
                    "amount": 10.00,
                    "description": "test transaction",
                    "type": "EXPENSE",
                    "date": "$testDate"
                }
            """.trimIndent()
            )
            contentType(Application.Json)
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(expectedCreatedTransactionResult, response.body())
    }

    @Test
    fun `Should return transaction by id with status code 200 when found`() = setupApplicationTest {
        transaction {
            Transactions.insert {
                it[this.amount] = BigDecimal("23.55")
                it[this.description] = "test transaction"
                it[this.type] = INCOME
                it[this.date] = testDate
            }
        }
        val expectedCreatedTransactionResult = """
                {
                    "id": 1,
                    "amount": "23.55",
                    "description": "test transaction",
                    "type": "INCOME",
                    "date": "$testDate"
                }
        """.trimIndent()

        val response: HttpResponse = client.get("$TRANSACTION_PATH/1")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expectedCreatedTransactionResult, response.body())
    }

    @Test
    fun `should return 404 not found when no transaction is found for id`() = setupApplicationTest {
        val transactionPathWithNonExistingId = "$TRANSACTION_PATH/15"
        val expectedProblemDetail = """
            {
                "title": "Resource not Found",
                "status": 404,
                "detail": "Transaction with id 15 not found",
                "instance": "$transactionPathWithNonExistingId"
            }
        """.trimIndent()

        val response = client.get(transactionPathWithNonExistingId)

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(expectedProblemDetail, response.body())
    }
}