package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.handleRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

private const val TRANSACTION_PATH = "/transaction"

fun Application.configureTransactionRouting() {
    val transactionService by closestDI().instance<TransactionService>()
    routing {
        route(TRANSACTION_PATH) {
            post {
                handleRequest(
                    { transactionService.saveTransaction(call.receive<CreateTransactionRequest>()) },
                    HttpStatusCode.Created
                )
            }
            get("{transactionSignature}") {
                handleRequest({
                    transactionService.findTransactionsBySignature(call.parameters.getOrFail("transactionSignature"))
                })
            }
        }
    }
}