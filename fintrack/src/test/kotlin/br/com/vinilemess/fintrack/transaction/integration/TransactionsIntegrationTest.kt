package br.com.vinilemess.fintrack.transaction.integration

import br.com.vinilemess.fintrack.common.ProblemDetail
import br.com.vinilemess.fintrack.transaction.*
import br.com.vinilemess.fintrack.transaction.TransactionType.INCOME
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.serializer
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

private const val TRANSACTION_PATH = "/transaction"

class TransactionsIntegrationTest : IntegrationTestSetup() {

    @Test
    fun `Should return created transaction with status 201 when a transaction is successfully saved`() = setupApplicationTest {
        val createTransactionRequest = defaultCreateTransactionRequest(type = TransactionType.INVESTMENT)

        val response = client.post(TRANSACTION_PATH) {
            setBody(deserializer.encodeToString(serializer(), createTransactionRequest))
            contentType(ContentType.Application.Json)
        }

        val deserializedTransaction = deserializer.decodeFromString<TransactionInfo>(response.body())

        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(1, deserializedTransaction.id)
        assertEquals(createTransactionRequest.description, deserializedTransaction.description)
        assertEquals("10.00", deserializedTransaction.amount.toString())
        assertEquals(createTransactionRequest.type, deserializedTransaction.type)
        assertEquals(createTransactionRequest.date, deserializedTransaction.date)
    }

    @Test
    fun `Should return transaction by id with status code 200 when found`() = setupApplicationTest {
        val amount = BigDecimal("23.55")
        val expectedTransactionInfo = defaultTransactionInfo(amount = amount)

        transaction {
            Transactions.insert {
                it[this.amount] = amount
                it[this.description] = "test transaction"
                it[this.type] = INCOME
                it[this.date] = testDate
            }
        }

        val response = client.get("$TRANSACTION_PATH/1")
        val deserializedTransaction = deserializer.decodeFromString<TransactionInfo>(response.body())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expectedTransactionInfo, deserializedTransaction)
    }

    @Test
    fun `should return 404 not found when no transaction is found for id`() = setupApplicationTest {
        val transactionPathWithNonExistingId = "$TRANSACTION_PATH/15"
        val expectedProblemDetail = ProblemDetail(
            title = "Resource not Found",
            status = 404,
            detail = "Transaction with id 15 not found",
            instance = transactionPathWithNonExistingId
        )

        val response = client.get(transactionPathWithNonExistingId)
        val deserializedProblemDetail = deserializer.decodeFromString<ProblemDetail>(response.body())

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(expectedProblemDetail, deserializedProblemDetail)
    }
}