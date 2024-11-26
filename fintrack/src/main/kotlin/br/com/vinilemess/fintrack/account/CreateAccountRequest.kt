package br.com.vinilemess.fintrack.account

import br.com.vinilemess.fintrack.common.serializers.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CreateAccountRequest(
    val type: AccountType,
    val name: String,
    @Serializable(with = BigDecimalSerializer::class)
    val initialBalance: BigDecimal = BigDecimal.ZERO
)
