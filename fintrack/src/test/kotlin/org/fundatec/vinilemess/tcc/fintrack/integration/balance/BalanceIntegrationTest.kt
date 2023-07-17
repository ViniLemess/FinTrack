package org.fundatec.vinilemess.tcc.fintrack.integration.balance

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.fundatec.vinilemess.tcc.fintrack.balance.domain.BalanceResponse
import org.fundatec.vinilemess.tcc.fintrack.integration.IntegrationTestSetup
import org.fundatec.vinilemess.tcc.fintrack.integration.TEST_URL_QUERY_PARAM_DATE
import org.fundatec.vinilemess.tcc.fintrack.testNegativeAmount
import org.fundatec.vinilemess.tcc.fintrack.testUserSignatureWithBalance
import org.fundatec.vinilemess.tcc.fintrack.testUserSignatureWithNegativeBalance
import org.fundatec.vinilemess.tcc.fintrack.testUserSignatureWithoutBalance
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.format.DateTimeFormatter.ISO_DATE

private const val BALANCE_URL = "/balances/{userSignature}"

class BalanceIntegrationTest : IntegrationTestSetup() {

    @BeforeEach
    fun populateDatabase() {
        insertUser(testUserSignatureWithBalance)
        insertUser(testUserSignatureWithoutBalance)
        insertUser(testUserSignatureWithNegativeBalance)
        insertTransactions()
        insertTransactions(2, testNegativeAmount)
    }

    @Test
    fun `Should return todays balance for GET balance without using date filter`() {
        val balanceResult = given()
            .pathParam("userSignature", testUserSignatureWithBalance)
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.valueOf(50), balanceResult.amount)
        assertEquals(getTodayDateAsString(), ISO_DATE.format(balanceResult.date))
    }

    @Test
    fun `Should return balance calculation for GET balance using date filter`() {
        val balanceResult = given()
            .pathParam("userSignature", testUserSignatureWithBalance)
            .queryParam("date", TEST_URL_QUERY_PARAM_DATE)
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.valueOf(50), balanceResult.amount)
        assertEquals(TEST_URL_QUERY_PARAM_DATE, ISO_DATE.format(balanceResult.date))
    }

    @Test
    fun `Should return 0 balance for GET balance when no transactions are found without date filter`() {
        val balanceResult = given()
            .pathParam("userSignature", testUserSignatureWithoutBalance)
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.ZERO, balanceResult.amount)
        assertEquals(getTodayDateAsString(), ISO_DATE.format(balanceResult.date))
    }

    @Test
    fun `Should return 0 balance for GET balance when no transactions are found before date filter`() {
        val balanceResult = given()
            .pathParam("userSignature", testUserSignatureWithoutBalance)
            .queryParam("date", TEST_URL_QUERY_PARAM_DATE)
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.ZERO, balanceResult.amount)
        assertEquals(TEST_URL_QUERY_PARAM_DATE, ISO_DATE.format(balanceResult.date))
    }

    @Test
    fun `Should return negative balance amount for GET when calculation returns a negative value without date filter`() {
        val balanceResult = given()
            .pathParam("userSignature", testUserSignatureWithNegativeBalance)
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.valueOf(-20), balanceResult.amount)
        assertEquals(getTodayDateAsString(), ISO_DATE.format(balanceResult.date))
    }

    @Test
    fun `Should return negative balance amount for GET when calculation returns a negative value with date filter`() {
        val balanceResult = given()
            .pathParam("userSignature", testUserSignatureWithNegativeBalance)
            .queryParam("date", TEST_URL_QUERY_PARAM_DATE)
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.valueOf(-20), balanceResult.amount)
        assertEquals(TEST_URL_QUERY_PARAM_DATE, ISO_DATE.format(balanceResult.date))
    }

    @AfterEach
    fun cleanTransactions() {
        clearTransactions()
    }
}