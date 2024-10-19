package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.serializers.BigDecimalSerializer
import br.com.vinilemess.fintrack.common.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.bson.BsonObjectId
import org.bson.codecs.pojo.annotations.BsonId
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    @BsonId
    val id: BsonObjectId? = null,
    val transactionSignature: String,
    val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    val date: LocalDateTime
) {
    fun copyWithId(id: BsonObjectId) = this.copy(id = id)
}

@Serializable
data class CreateTransactionRequest(
    val transactionSignature: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
)

fun CreateTransactionRequest.toTransaction() = Transaction(
    transactionSignature = this.transactionSignature,
    amount = this.amount,
    description = this.description,
    type = this.type,
    date = this.date
)

@Serializable
data class TransactionResponse(
    val transactionSignature: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
)

fun Transaction.toResponse() = TransactionResponse(
    transactionSignature = this.transactionSignature,
    amount = this.amount,
    description = this.description,
    type = this.type,
    date = this.date
)
