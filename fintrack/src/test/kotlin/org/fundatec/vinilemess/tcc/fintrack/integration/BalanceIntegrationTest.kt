package org.fundatec.vinilemess.tcc.fintrack.integration

import org.fundatec.vinilemess.tcc.fintrack.testUserSignature
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

private const val BALANCE_URL = "/balances/user-signature/{userSignature}"

class BalanceIntegrationTest: IntegrationTestSetup() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        insertTransactions()
    }

    @Test
    fun `Should return todays balance when for GET balance without using date filter`() {
        mockMvc.perform(get(BALANCE_URL, testUserSignature)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.amount").value(50))
            .andExpect(jsonPath("$.date").exists())
    }

    @AfterEach
    fun tearDown() {
        clearTransactions()
    }
}