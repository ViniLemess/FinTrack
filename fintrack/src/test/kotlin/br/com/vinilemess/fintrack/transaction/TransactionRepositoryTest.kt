package br.com.vinilemess.fintrack.transaction

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class TransactionRepositoryTest {
    private val transaction = Transaction(
        id = "1",
        amount = BigDecimal.TEN,
        description = "test transaction",
        type = TransactionType.INVESTMENT,
        date = LocalDateTime.MAX
    )
    private val transaction2 = Transaction(
        id = "2",
        amount = BigDecimal.TEN,
        description = "test transaction",
        type = TransactionType.EXPENSE,
        date = LocalDateTime.MAX
    )
    private val transactions = mutableMapOf(transaction.id to transaction, transaction2.id to transaction2)
    private val transactionRepository = TransactionRepository(transactions)

    @Test
    fun `should save new transaction`() {
        transactionRepository.saveTransaction(
            Transaction(
                amount = BigDecimal.TEN,
                description = "test transaction",
                type = TransactionType.INCOME,
                date = LocalDateTime.MAX
            )
        ).apply {
            assertNotNull(this.id)
            assertEquals(BigDecimal.TEN, this.amount)
            assertEquals("test transaction", this.description)
            assertEquals(TransactionType.INCOME, this.type)
            assertEquals(LocalDateTime.MAX, this.date)
        }
    }

    @Test
    fun `should return all saved transactions`() {
        transactionRepository.findTransactions().apply {
            assertEquals(transactions.values.toList(), this)
        }
    }

    @Test
    fun `should return transaction by id` () {
        transactionRepository.findTransaction("1").apply {
            assertEquals(transaction, this)
        }
    }
}