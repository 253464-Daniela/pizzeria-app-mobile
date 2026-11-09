package com.daniela.pizzeria.presentation.viewmodels

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daniela.pizzeria.PizzeriaApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AuthViewModel(pizzeriaApplication().container.authRepository)
        }
        initializer {
            HomeViewModel(pizzeriaApplication().container.productoRepository)
        }
        initializer {
            ProductoFormViewModel(pizzeriaApplication().container.productoRepository)
        }
    }
}

fun CreationExtras.pizzeriaApplication(): PizzeriaApplication =
    (this[APPLICATION_KEY] as PizzeriaApplication)