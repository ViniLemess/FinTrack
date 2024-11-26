package br.com.vinilemess.fintrack.account

import br.com.vinilemess.fintrack.common.database.CustomColumns.Companion.money
import br.com.vinilemess.fintrack.common.serializers.BigDecimalSerializer
import br.com.vinilemess.fintrack.common.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.math.BigDecimal
import java.time.LocalDateTime

object Accounts : Table(name = "accounts") {
    val id: Column<Long> = long("id").autoIncrement()
    val name: Column<String> = varchar("name", 72)
    val type: Column<AccountType> = enumeration("type", AccountType::class)
    val balance: Column<BigDecimal> = money("balance")
    val createdAt: Column<LocalDateTime> = datetime("created_at")
    val updatedAt: Column<LocalDateTime?> = datetime("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class AccountInfo(
    val id: Long,
    val name: String,
    val type: AccountType,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime?
)