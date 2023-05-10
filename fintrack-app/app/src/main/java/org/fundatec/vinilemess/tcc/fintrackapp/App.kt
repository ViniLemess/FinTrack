package org.fundatec.vinilemess.tcc.fintrackapp

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        private lateinit var instance: App
        val context: Context
            get() = instance
    }

    init {
        instance = this
    }
}

