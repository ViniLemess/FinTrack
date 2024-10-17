package br.com.vinilemess.fintrack.transaction

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TransactionServiceTest {

    private val repository = mockk<TransactionRepository>()
    private val service = TransactionService(repository)

    @Test
    fun `should save transaction`() {
        val createTransactionRequest = defaultCreateTransactionRequest()
        every { runBlocking { repository.saveTransaction(any()) } } returns defaultTransaction()

        runBlocking {
            service.saveTransaction(createTransactionRequest).onSuccess {
                assertEquals("transactionSignature", it.transactionSignature)
                assertEquals(TEST_DATE, it.date)
                assertEquals(TransactionType.INCOME, it.type)
                assertEquals("test transaction", it.description)
                assertEquals(BigDecimal.TEN.toString(), it.amount)
            }
        }
    }

    @Test
    fun `should return transactions with with provided signature`() {
        val transactions = listOf(
            defaultTransaction(transactionSignature = "1"),
            defaultTransaction(transactionSignature = "2"),
            defaultTransaction(transactionSignature = "3"),
        )
        val expectedTransactions = listOf(
            defaultTransactionResponse(transactionSignature = "1"),
            defaultTransactionResponse(transactionSignature = "2"),
            defaultTransactionResponse(transactionSignature = "3")
        )

        every { runBlocking { repository.findTransactionsBySignature(any()) } } returns transactions

        runBlocking {
            service.findTransactionsBySignature("transactionSignature").onSuccess {
                assertEquals(expectedTransactions, it)
            }
        }
    }
}