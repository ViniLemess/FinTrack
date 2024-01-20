package org.fundatec.vinilemess.tcc.fintrack.integration.transaction

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.fundatec.vinilemess.tcc.fintrack.integration.DATE_QUERY_NAME
import org.fundatec.vinilemess.tcc.fintrack.integration.EndToEndTestSetup
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.Recurrence
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.userSignature
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE

private const val TRANSACTIONS_PATH_USER_SIGNATURE = "/transactions/{userSignature}"

class TransactionEndToEndTest : EndToEndTestSetup() {

    @Test
    fun `Should return transactions ordenated by current date for GET without date filter`() {
        insertIncomeTransactions(userSignature = userSignature)
        mockMvc.perform(
            MockMvcRequestBuilders.get(TRANSACTIONS_PATH_USER_SIGNATURE, userSignature)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*]", hasSize<Any>(5)))
            .andExpect(jsonPath("$[*].id", everyItem(notNullValue())))
            .andExpect(jsonPath("$[*].userSignature", everyItem(equalTo(userSignature))))
            .andExpect(jsonPath("$[*].date", everyItem(notNullValue())))
            .andExpect(jsonPath("$[*].amount", everyItem(notNullValue())))
            .andExpect(jsonPath("$[*].description", everyItem(notNullValue())))
            .andExpect(jsonPath("$[*].transactionOperation", everyItem(oneOf("INCOME", "EXPENSE"))))
    }

    @Test
    fun `Should return transactions ordenated by date for GET with date filter`() {
        insertExpenseTransactions(3, userSignature = userSignature)
        mockMvc.perform(
            MockMvcRequestBuilders.get(TRANSACTIONS_PATH_USER_SIGNATURE, userSignature)
                .queryParam(DATE_QUERY_NAME, ISO_DATE.format(LocalDate.now()))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*]", hasSize<Any>(3)))
            .andExpect(jsonPath("$[*].id", everyItem(notNullValue())))
            .andExpect(jsonPath("$[*].userSignature", everyItem(equalTo(userSignature))))
            .andExpect(jsonPath("$[*].date", everyItem(notNullValue())))
            .andExpect(jsonPath("$[*].amount", everyItem(notNullValue())))
            .andExpect(jsonPath("$[*].description", everyItem(notNullValue())))
            .andExpect(jsonPath("$[*].transactionOperation", everyItem(oneOf("INCOME", "EXPENSE"))))
    }

    @Test
    fun `Should return empty list for GET when no transactions are found for the userSignature`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(TRANSACTIONS_PATH_USER_SIGNATURE, userSignature)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*]", hasSize<Any>(0)))
    }

    @Test
    fun `Should create a transaction successfully`() {
        val date = LocalDate.parse("2030-01-01")
        val description = "test transaction"
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TransactionRequest(BigDecimal.TEN, date, description, TransactionOperation.INCOME))
                .`when`()
                .post("/transactions/{userSignature}", userSignature)
                .then()
                .statusCode(HttpStatus.CREATED.value())

        val transactions = findAllTransactions()

        assertEquals(1, transactions.size)
        assertThat(transactions).satisfiesOnlyOnce { transaction ->
            assertEquals(BigDecimal.TEN, transaction.amount)
            assertEquals(date, transaction.date)
            assertEquals(TransactionOperation.INCOME, transaction.transactionOperation)
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
                operation = TransactionOperation.INCOME,
                frequency = 1,
                recurrenceCount = 10,
                recurrence = Recurrence.MONTHLY
        )
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(recurrentTransactionRequest)
                .`when`()
                .post("/transactions/recurrent/{userSignature}", userSignature)
                .then()
                .statusCode(HttpStatus.CREATED.value())

        val transactions = findAllTransactions().sortedBy { it.date }
        val firstTransaction = transactions[0]

        assertEquals(10, transactions.size)

        for(i in transactions.indices) {
            val transaction = transactions[i]
            assertEquals(BigDecimal.TEN, transaction.amount)
            assertEquals(inititalDate.plusMonths(i.toLong()), transaction.date)
            assertEquals(TransactionOperation.INCOME, transaction.transactionOperation)
            assertEquals(userSignature, transaction.userSignature)
            assertEquals(description, transaction.description)
            assertNotNull(transaction.id)
            assertEquals(firstTransaction.recurrenceId, transaction.recurrenceId)
        }
    }

    @AfterEach
    fun cleanTransactions() {
        clearTransactions()
    }
}