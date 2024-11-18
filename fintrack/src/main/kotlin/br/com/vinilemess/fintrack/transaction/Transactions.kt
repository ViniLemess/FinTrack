package br.com.vinilemess.fintrack.transaction

import br.com.vinilemess.fintrack.common.serializers.BigDecimalSerializer
import br.com.vinilemess.fintrack.common.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.math.BigDecimal
import java.time.LocalDateTime

object Transactions : Table("transactions") {
    val id: Column<Long> = long("id").autoIncrement()
    val amount: Column<BigDecimal> = decimal("amount", 18, 2)
    val description: Column<String> = varchar("description", 256)
    val type: Column<TransactionType> = enumeration("type", TransactionType::class)
    val date: Column<LocalDateTime> = datetime("date")

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class CreateTransactionRequest(
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
)

@Serializable
data class TransactionInfo(
    val id: Long,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
)
