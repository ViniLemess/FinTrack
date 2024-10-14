package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.handleRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

private const val TRANSACTION_PATH = "/transaction"

class TransactionController(private val service: TransactionService) {

    fun registerRoutes(routing: Routing) {
        routing.route(TRANSACTION_PATH) {
            post {
                handleRequest({
                    service.saveTransaction(call.receive<CreateTransactionRequest>()).getOrThrow()
                }, HttpStatusCode.Created)
            }
            get("{transactionSignature}") {
                handleRequest({
                    service.findTransactionsBySignature(call.parameters.getOrFail("transactionSignature"))
                })
            }
        }
    }
}