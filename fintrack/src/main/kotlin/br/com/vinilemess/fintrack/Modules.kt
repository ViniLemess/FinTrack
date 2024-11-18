package br.com.vinilemess.fintrack

import br.com.vinilemess.fintrack.configuration.PostgresProperties
import br.com.vinilemess.fintrack.transaction.TransactionRepository
import br.com.vinilemess.fintrack.transaction.TransactionService
import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import org.postgresql.ds.PGSimpleDataSource

object Modules {
    fun initializeDependencies(postgresProperties: PostgresProperties) = DI.Module("appModule") {
        bind<PGSimpleDataSource>() with singleton {
            postgresProperties.let {
                PGSimpleDataSource().apply {
                    setUrl("jdbc:postgresql://${it.host}:${it.port}/${it.database}")
                    user = it.username
                    password = it.password
                    databaseName = it.database
                }
            }
        }
        bind<Database>() with singleton { Database.connect(instance<PGSimpleDataSource>()) }
        bind<TransactionRepository>() with singleton { TransactionRepository(instance()) }
        bind<TransactionService>() with singleton { TransactionService(instance()) }
    }
}