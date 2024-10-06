package br.com.vinilemess.fintrack

import org.kodein.di.*
import transaction.TransactionController
import transaction.TransactionRepository
import transaction.TransactionService

object Modules {
    val appModule = DI.Module("appModule") {
        bind<TransactionRepository>().with(singleton { TransactionRepository() })
        bind<TransactionService>().with(singleton { TransactionService(instance()) })
        bind<TransactionController>().with(singleton { TransactionController(instance()) })
    }
}