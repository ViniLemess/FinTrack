package br.com.vinilemess.fintrack.configuration

import br.com.vinilemess.fintrack.transaction.Transactions
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureDatabaseTables() {
    closestDI().direct.instance<Database>().apply {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Transactions)
        }
    }
}