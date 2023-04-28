package org.fundatec.vinilemess.tcc.fintrack.transaction.service

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.fundatec.vinilemess.tcc.fintrack.*
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.Recurrence
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.repository.TransactionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class TransactionServiceTest {

    private val transactionRepository: TransactionRepository = mockk()
    private val transactionService = TransactionService(transactionRepository)

    @Test
    fun `Should not throw exception when transact is called`() {
        val transaction = TransactionRequest(testPositiveAmount, testDate, testDescription, TransactionOperation.INCOME)
        justRun { transactionRepository.save(any()) }

        assertDoesNotThrow { transactionService.transact(transaction, testUserSignature) }

        verify { transactionRepository.save(any()) }
    }

    @Test
    fun `Should call repository save 1 time when transactRecurrence is called for any recurrenceCount`() {
        val recurrentTransaction = RecurrentTransactionRequest(
            testPositiveAmount,
            testDate,
            testDescription,
            TransactionOperation.INCOME,
            Recurrence.MONTHLY,
            1,
            10
        )
        justRun { transactionRepository.saveAll(any()) }

        assertDoesNotThrow { transactionService.transactRecurrence(recurrentTransaction, testUserSignature) }

        verify { transactionRepository.saveAll(any()) }
    }

    @Test
    fun `Should return transactions mapped to response when method listTransactionsBeforeDateByUserSignature is called`() {
        val transactions = listOf(
            Transaction(testId, testUserSignature, null, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            Transaction(testId, testUserSignature, null, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            Transaction(testId, testUserSignature, null, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME)
        )
        every { transactionRepository.findTransactionsBeforeDateByUserSignature(testUserSignature, testDate) } returns transactions

        val result = transactionService.listTransactionsBeforeDateByUserSignature(testUserSignature, testDate)

        assertThat(result).allSatisfy {
            assertEquals(testId, it.id)
            assertEquals(testUserSignature, it.userSignature)
            assertNull(it.recurrenceId)
            assertEquals(testDate, it.date)
            assertEquals(testPositiveAmount, it.amount)
            assertEquals(testDescription, it.description)
            assertEquals(TransactionOperation.INCOME, it.transactionOperation)
        }

        verify { transactionRepository.findTransactionsBeforeDateByUserSignature(testUserSignature, testDate) }
    }

    @Test
    fun `Should call repository delete without throwing exception when deleteById is called`() {
        justRun { transactionRepository.deleteById(id = testId) }

        assertDoesNotThrow { transactionService.deleteTransactionById(id = testId) }

        verify { transactionRepository.deleteById(id = testId) }
    }
}