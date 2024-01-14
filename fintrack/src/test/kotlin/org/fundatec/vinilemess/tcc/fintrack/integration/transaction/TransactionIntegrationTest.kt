package org.fundatec.vinilemess.tcc.fintrack.integration.transaction

import org.fundatec.vinilemess.tcc.fintrack.integration.DATE_QUERY_NAME
import org.fundatec.vinilemess.tcc.fintrack.integration.IntegrationTestSetup
import org.fundatec.vinilemess.tcc.fintrack.userSignature
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE

private const val TRANSACTIONS_PATH_USER_SIGNATURE = "/transactions/{userSignature}"

class TransactionIntegrationTest : IntegrationTestSetup() {

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

    @AfterEach
    fun cleanTransactions() {
        clearTransactions()
    }
}