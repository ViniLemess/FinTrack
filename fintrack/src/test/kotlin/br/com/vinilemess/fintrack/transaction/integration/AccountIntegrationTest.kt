package br.com.vinilemess.fintrack.transaction.integration

import br.com.vinilemess.fintrack.account.AccountInfo
import br.com.vinilemess.fintrack.account.AccountType
import br.com.vinilemess.fintrack.account.CreateAccountRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

private const val ACCOUNT_PATH = "/account"

class AccountIntegrationTest: IntegrationTestSetup() {

    @Test
    fun `should create account successfully`() = setupApplicationTest {
        val createAccountRequest = CreateAccountRequest(
            type = AccountType.EXPENSES,
            name = "Groceries",
            initialBalance = BigDecimal("100.00")
        )

        val response = client.post(ACCOUNT_PATH) {
            setBody(deserializer.encodeToString(CreateAccountRequest.serializer(), createAccountRequest))
            contentType(Application.Json)
        }

        val accountInfo = deserializer.decodeFromString<AccountInfo>(response.body())

        assertEquals(1, accountInfo.id)
        assertEquals(response.status, HttpStatusCode.Created)
        assertEquals("Groceries", accountInfo.name)
        assertEquals(BigDecimal("100.00"), accountInfo.balance)
        assertEquals(AccountType.EXPENSES, accountInfo.type)
    }
}