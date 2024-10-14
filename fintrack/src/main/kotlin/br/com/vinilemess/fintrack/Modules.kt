package br.com.vinilemess.fintrack

import br.com.vinilemess.fintrack.configuration.MongoProperties
import br.com.vinilemess.fintrack.transaction.TransactionController
import br.com.vinilemess.fintrack.transaction.TransactionRepository
import br.com.vinilemess.fintrack.transaction.TransactionService
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object Modules {
    fun initializeDependencies(mongoProperties: MongoProperties) = DI.Module("appModule") {
        bind<MongoClient>() with singleton { MongoClient.create(mongoProperties.generateConnectionStringWithAuthentication()) }
        bind<MongoDatabase>() with singleton { instance<MongoClient>().getDatabase(mongoProperties.database) }
        bind<TransactionRepository>() with singleton { TransactionRepository(instance()) }
        bind<TransactionService>() with singleton { TransactionService(instance()) }
        bind<TransactionController>() with singleton { TransactionController(instance()) }
    }
}