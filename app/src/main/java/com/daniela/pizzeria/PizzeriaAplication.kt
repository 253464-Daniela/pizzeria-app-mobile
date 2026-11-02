package com.daniela.pizzeria

import android.app.Application

class PizzeriaApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}