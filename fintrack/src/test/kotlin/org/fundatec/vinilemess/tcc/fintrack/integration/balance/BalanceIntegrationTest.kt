package org.fundatec.vinilemess.tcc.fintrack.integration.balance

import org.fundatec.vinilemess.tcc.fintrack.integration.DATE_QUERY_NAME
import org.fundatec.vinilemess.tcc.fintrack.integration.IntegrationTestSetup
import org.fundatec.vinilemess.tcc.fintrack.integration.TEST_URL_QUERY_PARAM_DATE
import org.fundatec.vinilemess.tcc.fintrack.testNegativeAmount
import org.fundatec.vinilemess.tcc.fintrack.testUserSignature
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

private const val BALANCE_URL = "/balances/user-signature/{userSignature}"

class BalanceIntegrationTest : IntegrationTestSetup() {

    @Test
    fun `Should return todays balance for GET balance without using date filter`() {
        insertTransactions()
        mockMvc.perform(
            MockMvcRequestBuilders.get(BALANCE_URL, testUserSignature)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(50))
            .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(getTodayDateAsString()))
    }

    @Test
    fun `Should return balance calculation for GET balance using date filter`() {
        insertTransactions()
        mockMvc.perform(
            MockMvcRequestBuilders.get(BALANCE_URL, testUserSignature)
                .queryParam(DATE_QUERY_NAME, TEST_URL_QUERY_PARAM_DATE)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(50))
            .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(TEST_URL_QUERY_PARAM_DATE))
    }

    @Test
    fun `Should return 0 balance for GET balance when no transactions are found without date filter`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(BALANCE_URL, testUserSignature)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(getTodayDateAsString()))
    }

    @Test
    fun `Should return 0 balance for GET balance when no transactions are found before date filter`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(BALANCE_URL, testUserSignature)
                .queryParam(DATE_QUERY_NAME, TEST_URL_QUERY_PARAM_DATE)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(TEST_URL_QUERY_PARAM_DATE))
    }

    @Test
    fun `Should return negative balance amount for GET when calculation returns a negative value without date filter`() {
        insertTransactions(2, testNegativeAmount)
        mockMvc.perform(
            MockMvcRequestBuilders.get(BALANCE_URL, testUserSignature)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(-20))
            .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(getTodayDateAsString()))
    }

    @Test
    fun `Should return negative balance amount for GET when calculation returns a negative value with date filter`() {
        insertTransactions(2, testNegativeAmount)
        mockMvc.perform(
            MockMvcRequestBuilders.get(BALANCE_URL, testUserSignature)
                .queryParam(DATE_QUERY_NAME, TEST_URL_QUERY_PARAM_DATE)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(-20))
            .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(TEST_URL_QUERY_PARAM_DATE))
    }

    @AfterEach
    fun cleanTransactions() {
        clearTransactions()
    }
}