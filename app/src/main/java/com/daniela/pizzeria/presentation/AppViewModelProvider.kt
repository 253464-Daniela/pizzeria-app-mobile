package com.daniela.pizzeria.presentation

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daniela.pizzeria.PizzeriaApplication
import com.daniela.pizzeria.presentation.auth.AuthViewModel
import com.daniela.pizzeria.presentation.home.HomeViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AuthViewModel(pizzeriaApplication().container.authRepository)
        }
        initializer {
            HomeViewModel(pizzeriaApplication().container.productoRepository)
        }
    }
}

// Extensión para obtener la Application
fun CreationExtras.pizzeriaApplication(): PizzeriaApplication =
    (this[APPLICATION_KEY] as PizzeriaApplication)