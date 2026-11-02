package com.daniela.pizzeria.presentation.auth

// Un solo objeto para manejar todo el estado de Login y Register
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "", // Solo para Register
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false, // Para saber si navegar al Home
    val isLoginMode: Boolean = true // true = Login, false = Register
)