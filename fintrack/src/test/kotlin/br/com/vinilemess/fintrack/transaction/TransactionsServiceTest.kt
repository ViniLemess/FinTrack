package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.ApiResult
import br.com.vinilemess.fintrack.common.ApiResult.Failure
import br.com.vinilemess.fintrack.common.ApiResult.Success
import br.com.vinilemess.fintrack.common.ProblemDetail
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TransactionsServiceTest {

    private val repository = mockk<TransactionRepository>()
    private val service = TransactionService(repository)

    @Test
    fun `should save transaction`() {
        val createTransactionRequest = defaultCreateTransactionRequest()
        every { runBlocking { repository.saveTransaction(any()) } } returns defaultTransactionInfo()

        runBlocking {
            service.saveTransaction(createTransactionRequest).onSuccess {
                assertEquals(1, it.id)
                assertEquals(testDate, it.date)
                assertEquals(TransactionType.INCOME, it.type)
                assertEquals("test transaction", it.description)
                assertEquals(BigDecimal.TEN, it.amount)
            }
        }
    }

    @Test
    fun `should return transaction when find by id`() {
        val defaultTransactionInfo: TransactionInfo = defaultTransactionInfo()
        every { runBlocking { repository.findTransactionById(1L) } } returns defaultTransactionInfo

        runBlocking {
            val result: ApiResult<TransactionInfo> = service.findTransactionById(1)

            assertEquals(Success(defaultTransactionInfo), result)
        }
    }

    @Test
    fun `should return 404 not found when no transaction is found for id`() {
        val expectedProblemDetail = ProblemDetail(
            title = "Resource not Found",
            status = 404,
            detail = "Transaction with id 1 not found"
        )
        every { runBlocking { repository.findTransactionById(1L) } } returns null

        runBlocking {
            val result: ApiResult<TransactionInfo> = service.findTransactionById(1)

            assertEquals(Failure(expectedProblemDetail), result)
        }
    }
}