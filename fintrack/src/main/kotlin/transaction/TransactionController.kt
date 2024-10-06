package transaction

import br.com.vinilemess.fintrack.ProblemDetail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.net.URI

private const val TRANSACTION_PATH = "/transaction"

class TransactionController(private val service: TransactionService) {

    fun registerRoutes(routing: Routing) {
        routing.route(TRANSACTION_PATH) {
            post {
                call.respond(
                    status = HttpStatusCode.Created,
                    message = service.saveTransaction(call.receive<CreateTransactionRequest>())
                )
            }
            get {
                call.respond(service.findAllTransactions())
            }
            get("{id}") {
                val transaction: TransactionResponse? = service.findTransaction(id = call.parameters.getOrFail("id"))
                transaction?.let { call.respond(it) } ?: call.respond(
                    HttpStatusCode.NotFound, ProblemDetail(
                        title = "Resource not found",
                        status = 404,
                        detail = "Transaction not found",
                        instance = URI.create(TRANSACTION_PATH)
                    )
                )
            }
        }
    }
}