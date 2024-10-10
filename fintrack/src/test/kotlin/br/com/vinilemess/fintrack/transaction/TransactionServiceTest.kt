package br.com.vinilemess.fintrack.transaction

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME

class TransactionServiceTest {

    private val repository = mockk<TransactionRepository>()
    private val service = TransactionService(repository)

    @Test
    fun `should save transaction`() {
        val createTransactionRequest = defaultCreateTransactionRequest()

        every { repository.saveTransaction(any()) } returns defaultTransaction()

        val transactionResponse = service.saveTransaction(createTransactionRequest)

        assertNotNull(transactionResponse.id)
        assertEquals(ISO_DATE_TIME.format(LocalDateTime.MAX), transactionResponse.date)
        assertEquals(TransactionType.INCOME, transactionResponse.type)
        assertEquals("test transaction", transactionResponse.description)
        assertEquals(BigDecimal.TEN.toString(), transactionResponse.amount)
    }

    @Test
    fun testFindTransaction() {
        val transaction = defaultTransaction()
        val expectedTransactionResponse = defaultTransactionResponse()

        every { repository.findTransaction(any()) } returns transaction

        val foundTransaction = service.findTransaction("transactionId")

        assertEquals(expectedTransactionResponse, foundTransaction)
    }

    @Test
    fun testFindAllTransactions() {
        val transactions = listOf(
            defaultTransaction(id = "1"),
            defaultTransaction(id = "2"),
            defaultTransaction(id = "3"),
        )
        val expectedTransactions = listOf(
            defaultTransactionResponse(id = "1"),
            defaultTransactionResponse(id = "2"),
            defaultTransactionResponse(id = "3")
        )
        every { repository.findTransactions() } returns transactions

        val foundTransactions = service.findAllTransactions()

        assertEquals(3, foundTransactions.size)
        assertEquals(expectedTransactions, foundTransactions)
    }
}