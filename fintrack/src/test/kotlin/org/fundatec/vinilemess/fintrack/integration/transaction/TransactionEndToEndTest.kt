package org.fundatec.vinilemess.fintrack.integration.transaction

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.fundatec.vinilemess.fintrack.integration.DATE_QUERY_NAME
import org.fundatec.vinilemess.fintrack.integration.EndToEndTestSetup
import org.fundatec.vinilemess.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.fintrack.transaction.domain.enums.Recurrence
import org.fundatec.vinilemess.fintrack.transaction.domain.enums.TransactionOperation.EXPENSE
import org.fundatec.vinilemess.fintrack.transaction.domain.enums.TransactionOperation.INCOME
import org.fundatec.vinilemess.fintrack.transaction.domain.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.fintrack.transaction.domain.request.TransactionRequest
import org.fundatec.vinilemess.fintrack.userSignature
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE

private const val TRANSACTIONS_PATH_USER_SIGNATURE = "/transactions/{userSignature}"

class TransactionEndToEndTest : EndToEndTestSetup() {

    @Test
    fun `Should return transactions ordered by current date for GET without date filter`() {
        insertIncomeTransactions(userSignature = userSignature)
        insertExpenseTransactions(userSignature = userSignature)

        val transactions: List<Transaction> = given()
                .contentType(ContentType.JSON)
                .get(TRANSACTIONS_PATH_USER_SIGNATURE, userSignature)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getList("$", Transaction::class.java)

        assertEquals(10, transactions.size)
        assertThat(transactions).allSatisfy { transaction ->
            assertNotNull(transaction.id)
            assertEquals(userSignature, transaction.userSignature)
            assertNotNull(transaction.date)
            assertNotNull(transaction.amount)
            assertTrue(transaction.transactionOperation in listOf(INCOME, EXPENSE))
        }
    }

    @Test
    fun `Should return transactions ordered by date for GET with date filter`() {
        insertExpenseTransactions(3, userSignature = userSignature)

        val transactions = given()
                .contentType(ContentType.JSON)
                .queryParam(DATE_QUERY_NAME, ISO_DATE.format(LocalDate.now()))
                .get(TRANSACTIONS_PATH_USER_SIGNATURE, userSignature)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getList("$", Transaction::class.java)

        assertEquals(3, transactions.size)
        assertThat(transactions).allSatisfy { transaction ->
            assertNotNull(transaction.id)
            assertEquals(userSignature, transaction.userSignature)
            assertNotNull(transaction.date)
            assertNotNull(transaction.amount)
            assertTrue(transaction.transactionOperation in listOf(INCOME, EXPENSE))
        }
    }

    @Test
    fun `Should return empty list for GET when no transactions are found for the userSignature`() {
        given()
                .contentType(ContentType.JSON)
                .get(TRANSACTIONS_PATH_USER_SIGNATURE, userSignature)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize<Any>(0))
    }

    @Test
    fun `Should create a transaction successfully`() {
        val date = LocalDate.parse("2030-01-01")
        val description = "test transaction"
        given()
                .contentType(ContentType.JSON)
                .body(TransactionRequest(BigDecimal.TEN, date, description, INCOME))
                .`when`()
                .post(TRANSACTIONS_PATH_USER_SIGNATURE, userSignature)
                .then()
                .statusCode(HttpStatus.CREATED.value())

        val transactions = findAllTransactions()

        assertEquals(1, transactions.size)
        assertThat(transactions).satisfiesOnlyOnce { transaction ->
            assertEquals(BigDecimal.TEN, transaction.amount)
            assertEquals(date, transaction.date)
            assertEquals(INCOME, transaction.transactionOperation)
            assertEquals(userSignature, transaction.userSignature)
            assertEquals(description, transaction.description)
            assertNotNull(transaction.id)
        }
    }

    @Test
    fun `Should create a recurrent transaction successfully`() {
        val description = "test transaction"
        val inititalDate = LocalDate.parse("2030-01-01")
        val recurrentTransactionRequest = RecurrentTransactionRequest(
                amount = BigDecimal.TEN,
                inititalDate = inititalDate,
                description = description,
                operation = INCOME,
                frequency = 1,
                recurrenceCount = 10,
                recurrence = Recurrence.MONTHLY
        )
        given()
                .contentType(ContentType.JSON)
                .body(recurrentTransactionRequest)
                .`when`()
                .post("/transactions/recurrent/{userSignature}", userSignature)
                .then()
                .statusCode(HttpStatus.CREATED.value())

        val transactions = findAllTransactions().sortedBy { it.date }
        val firstTransaction = transactions[0]

        assertEquals(10, transactions.size)

        for (i in transactions.indices) {
            val transaction = transactions[i]
            assertEquals(BigDecimal.TEN, transaction.amount)
            assertEquals(inititalDate.plusMonths(i.toLong()), transaction.date)
            assertEquals(INCOME, transaction.transactionOperation)
            assertEquals(userSignature, transaction.userSignature)
            assertEquals(description, transaction.description)
            assertNotNull(transaction.id)
            assertEquals(firstTransaction.recurrenceId, transaction.recurrenceId)
        }
    }

    @Test
    fun `Should delete transactions successfully`() {
        insertIncomeTransactions(amountToInsert = 1, userSignature = userSignature)
        val transaction = findAllTransactions()[0]

        given()
                .contentType(ContentType.JSON)
                .`when`()
                .params("id", listOf(transaction.id))
                .delete("/transactions")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())

        val transactions = findAllTransactions()

        assertEquals(listOf<Transaction>(), transactions)
    }

    @AfterEach
    fun cleanTransactions() {
        clearTransactions()
    }
}