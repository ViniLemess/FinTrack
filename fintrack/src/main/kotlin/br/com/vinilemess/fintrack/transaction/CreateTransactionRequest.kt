package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.serializers.BigDecimalSerializer
import br.com.vinilemess.fintrack.common.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class CreateTransactionRequest(
    @property:Serializable(with = BigDecimalSerializer::class) val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    @property:Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime = LocalDateTime.now(),
)