package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.handleRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.time.LocalDate
import java.time.LocalDate.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

private const val TRANSACTION_PATH = "/transaction"

fun Application.configureTransactionRouting() {
    val transactionService: TransactionService by closestDI().instance<TransactionService>()

    routing {
        route(TRANSACTION_PATH) {
            post {
                handleRequest(
                    { transactionService.saveTransaction(call.receive<CreateTransactionRequest>()) },
                    HttpStatusCode.Created
                )
            }
            get("/{id}") {
                handleRequest({
                    transactionService.findTransactionById(call.parameters.getOrFail("id").toLong())
                })
            }
            get("/balance") {
                handleRequest({
                    transactionService.projectBalanceAtDate(LocalDate.now())
                })
            }
            get("/balance/{date}") {
                handleRequest({
                    transactionService.projectBalanceAtDate(call.parameters.getOrFail("date").let { parse(it, ISO_LOCAL_DATE) })
                })
            }
        }
    }
}