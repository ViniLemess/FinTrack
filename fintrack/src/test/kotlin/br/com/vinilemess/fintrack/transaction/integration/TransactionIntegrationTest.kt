package br.com.vinilemess.fintrack.transaction.integration

import br.com.vinilemess.fintrack.transaction.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Test
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.math.BigDecimal.TEN
import kotlin.test.assertEquals

private const val TRANSACTION_PATH = "/transaction"

class TransactionIntegrationTest : IntegrationTestSetup() {

    @Test
    fun `Should return created transaction with 201 status when a transaction is sucessfully saved`() =
        integrationTestApplication {
            val createTransactionRequest = defaultCreateTransactionRequest(type = TransactionType.INVESTMENT)

            val response = client.post(TRANSACTION_PATH) {
                setBody(deserializer.encodeToString(serializer(), createTransactionRequest))
                contentType(ContentType.Application.Json)
            }

            val deserializedTransaction = deserializer.decodeFromString<TransactionResponse>(response.body())

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals("transactionSignature", deserializedTransaction.transactionSignature)
            assertEquals(createTransactionRequest.description, deserializedTransaction.description)
            assertEquals(createTransactionRequest.amount, deserializedTransaction.amount)
            assertEquals(createTransactionRequest.type, deserializedTransaction.type)
            assertEquals(createTransactionRequest.date, deserializedTransaction.date)
        }

    @Test
    fun `Should return all saved transaction for transaction signature with status 200`() = integrationTestApplication {
        application {
            val transactionRepository: TransactionRepository by closestDI().instance<TransactionRepository>()
            runBlocking {
                transactionRepository.saveTransaction(defaultTransaction(transactionSignature = "testSignature"))
                transactionRepository.saveTransaction(defaultTransaction())
                transactionRepository.saveTransaction(defaultTransaction(transactionSignature = "testSignature"))
            }
        }

        val response = client.get("$TRANSACTION_PATH/testSignature")

        val deserializedTransactions = deserializer.decodeFromString<List<TransactionResponse>>(response.body())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(2, deserializedTransactions.size)
        deserializedTransactions.forEach { deserializedTransaction ->
            assertEquals("testSignature", deserializedTransaction.transactionSignature)
            assertEquals("test transaction", deserializedTransaction.description)
            assertEquals(TEN.toString(), deserializedTransaction.amount)
            assertEquals(TransactionType.INCOME, deserializedTransaction.type)
            assertEquals(TEST_DATE, deserializedTransaction.date)
        }
    }
}