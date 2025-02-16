package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.serializers.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class BalanceInfo(
    @Serializable(with = BigDecimalSerializer::class) val balance: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class) val incomingAmount: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class) val outgoingAmount: BigDecimal
)
