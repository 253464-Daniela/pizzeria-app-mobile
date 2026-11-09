package com.daniela.pizzeria

import android.app.Application
import com.daniela.pizzeria.di.AppContainer

class PizzeriaApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}