package org.fundatec.vinilemess.fintrack.integration.balance

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.fundatec.vinilemess.fintrack.balance.BalanceResponse
import org.fundatec.vinilemess.fintrack.integration.EndToEndTestSetup
import org.fundatec.vinilemess.fintrack.userSignature
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE

private const val BALANCE_URL = "/balances/{userSignature}"

class BalanceEndToEndTest : EndToEndTestSetup() {

    @BeforeEach
    fun populateDatabase() {
        insertUser(userSignature)
    }

    @Test
    @WithMockUser(username = "johndoe@test.com", roles = ["USER"])
    fun `Should return todays balance for GET balance without using date filter`() {
        insertIncomeTransactions(userSignature = userSignature)
        insertIncomeTransactions(userSignature = userSignature, date = LocalDate.now().plusDays(10))

        val balanceResult = given()
            .spec(authenticatedRequestSpec())
            .pathParam("userSignature", userSignature)
            .`when`()
            .get(BALANCE_URL)
            .then()
            .log()
            .ifValidationFails()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.valueOf(50), balanceResult.amount)
        assertEquals(getTodayDateAsString(), ISO_DATE.format(balanceResult.date))
    }

    @Test
    fun `Should return balance calculation for GET balance using date filter`() {
        insertIncomeTransactions(userSignature = userSignature)
        val tenDaysFromNow = LocalDate.now().plusDays(10)
        insertIncomeTransactions(userSignature = userSignature, date = tenDaysFromNow)

        val balanceResult = given()
            .spec(authenticatedRequestSpec())
            .pathParam("userSignature", userSignature)
            .queryParam("date", ISO_DATE.format(tenDaysFromNow))
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.valueOf(100), balanceResult.amount)
        assertEquals(tenDaysFromNow.toString(), ISO_DATE.format(balanceResult.date))
    }

    @Test
    fun `Should return 0 balance for GET balance when no transactions are found without date filter`() {
        val tenDaysFromNow = LocalDate.now().plusDays(10)
        insertIncomeTransactions(userSignature = userSignature, date = tenDaysFromNow)

        val balanceResult = given()
            .spec(authenticatedRequestSpec())
            .pathParam("userSignature", userSignature)
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
        val tenDaysFromNow = LocalDate.now().plusDays(10)
        val nineDaysFromNow = LocalDate.now().plusDays(9)
        insertIncomeTransactions(userSignature = userSignature, date = tenDaysFromNow)

        val balanceResult = given()
            .spec(authenticatedRequestSpec())
            .pathParam("userSignature", userSignature)
            .queryParam("date", ISO_DATE.format(nineDaysFromNow))
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.ZERO, balanceResult.amount)
        assertEquals(ISO_DATE.format(nineDaysFromNow), ISO_DATE.format(balanceResult.date))
    }

    @Test
    fun `Should return negative balance amount for GET when calculation returns a negative value without date filter`() {
        insertExpenseTransactions(amountToInsert = 2, userSignature = userSignature)

        val balanceResult = given()
            .spec(authenticatedRequestSpec())
            .pathParam("userSignature", userSignature)
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
        val tenDaysFromNow = LocalDate.now().plusDays(10)
        insertExpenseTransactions(amountToInsert = 2, userSignature = userSignature, date = tenDaysFromNow)

        val balanceResult = given()
            .spec(authenticatedRequestSpec())
            .pathParam("userSignature", userSignature)
            .queryParam("date", ISO_DATE.format(tenDaysFromNow))
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(BalanceResponse::class.java)

        assertEquals(BigDecimal.valueOf(-20), balanceResult.amount)
        assertEquals(ISO_DATE.format(tenDaysFromNow), ISO_DATE.format(balanceResult.date))
    }

    @Test
    fun `Should return 404 resource not found for GET when userSignature does not exists`() {
        val nonExistentUserSignature = "nonExistentSignature"

        given()
            .spec(authenticatedRequestSpec())
            .pathParam("userSignature", nonExistentUserSignature)
            .`when`()
            .get(BALANCE_URL)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .contentType(ContentType.JSON)
            .body("title", equalTo("Resource not found"))
            .body("detail", equalTo("The provided user signature does not exist!"))
            .body("type", equalTo("https://www.rfc-editor.org/rfc/rfc7807"))
            .body("timestamp", notNullValue())
    }

    @AfterEach
    fun cleanTransactions() {
        clearTransactions()
    }
}