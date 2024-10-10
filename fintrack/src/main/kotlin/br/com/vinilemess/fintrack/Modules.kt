package br.com.vinilemess.fintrack

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import br.com.vinilemess.fintrack.transaction.TransactionController
import br.com.vinilemess.fintrack.transaction.TransactionRepository
import br.com.vinilemess.fintrack.transaction.TransactionService

object Modules {
    val appModule = DI.Module("appModule") {
        bind<TransactionRepository>() with singleton { TransactionRepository() }
        bind<TransactionService>() with singleton { TransactionService(instance()) }
        bind<TransactionController>() with singleton { TransactionController(instance()) }
    }
}