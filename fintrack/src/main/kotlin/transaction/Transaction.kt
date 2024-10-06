package transaction

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.joda.time.LocalDateTime
import java.math.BigDecimal

data class Transaction(
    val id: String? = null,
    val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    val date: LocalDateTime
)

@Serializable
data class CreateTransactionRequest(
    @Contextual val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    @Contextual val date: LocalDateTime = LocalDateTime.now()
)

fun createTransactionFromRequest(createTransactionRequest: CreateTransactionRequest) = Transaction(
    amount = createTransactionRequest.amount,
    description = createTransactionRequest.description,
    type = createTransactionRequest.type,
    date = createTransactionRequest.date
)

@Serializable
data class TransactionResponse(
    val id: String?,
    @Contextual val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    @Contextual val date: LocalDateTime,
)

fun Transaction.toResponse() = TransactionResponse(
    id = this.id,
    amount = this.amount,
    description = this.description,
    type = this.type,
    date = this.date
)
