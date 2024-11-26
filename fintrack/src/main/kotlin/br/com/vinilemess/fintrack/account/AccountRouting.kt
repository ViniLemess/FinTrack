package br.com.vinilemess.fintrack.account

import br.com.vinilemess.fintrack.common.handleRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

private const val ACCOUNT_PATH = "/account"

fun Application.configureAccountRouting() {
    val accountService: AccountService by closestDI().instance()

    routing {
        route(ACCOUNT_PATH) {
            post {
                handleRequest(
                    { accountService.saveAccount(call.receive()) },
                    HttpStatusCode.Created
                )
            }
        }
    }
}