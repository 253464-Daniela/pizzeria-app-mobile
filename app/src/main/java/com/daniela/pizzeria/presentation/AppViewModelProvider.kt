package com.daniela.pizzeria.presentation

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daniela.pizzeria.PizzeriaApplication
import com.daniela.pizzeria.presentation.auth.AuthViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AuthViewModel(pizzeriaApplication().container.authRepository)
        }
        // Aquí agregaremos el ProductoViewModel más adelante...
    }
}

// Extensión para obtener la Application
fun CreationExtras.pizzeriaApplication(): PizzeriaApplication =
    (this[APPLICATION_KEY] as PizzeriaApplication)