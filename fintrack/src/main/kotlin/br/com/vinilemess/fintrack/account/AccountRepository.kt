package br.com.vinilemess.fintrack.account

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class AccountRepository(val database: Database) {

    suspend fun saveAccount(createAccountRequest: CreateAccountRequest): AccountInfo {
        return newSuspendedTransaction(db = database) {
            Accounts.insertReturning {
                it[name] = createAccountRequest.name
                it[type] = createAccountRequest.type
                it[balance] = createAccountRequest.initialBalance
                it[createdAt] = LocalDateTime.now()
            }.map(::toAccountInfo).first()
        }
    }

    private fun toAccountInfo(result: ResultRow): AccountInfo = AccountInfo(
        id = result[Accounts.id],
        name = result[Accounts.name],
        type = result[Accounts.type],
        balance = result[Accounts.balance],
        createdAt = result[Accounts.createdAt],
        updatedAt = result[Accounts.updatedAt]
    )
}