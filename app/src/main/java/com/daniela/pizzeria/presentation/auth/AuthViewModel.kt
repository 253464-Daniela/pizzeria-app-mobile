package com.daniela.pizzeria.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniela.pizzeria.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // El estado mutable es PRIVADO como pide la rúbrica
    private val _uiState = MutableStateFlow(AuthUiState())
    // El estado expuesto es INMUTABLE (asStateFlow)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Funciones para actualizar los campos desde la UI
    fun onEmailChange(email: String) = _uiState.update { it.copy(email = email, error = null) }
    fun onPasswordChange(password: String) = _uiState.update { it.copy(password = password, error = null) }
    fun onConfirmPasswordChange(confirm: String) = _uiState.update { it.copy(confirmPassword = confirm, error = null) }

    fun toggleMode() {
        _uiState.update {
            it.copy(
                isLoginMode = !it.isLoginMode,
                error = null,
                email = "",
                password = "",
                confirmPassword = ""
            )
        }
    }

    fun submit() {
        val state = _uiState.value

        // VALIDACIONES (Requisito de la rúbrica)
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Todos los campos son obligatorios") }
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.update { it.copy(error = "Correo electrónico inválido") }
            return
        }
        if (state.password.length < 8) {
            _uiState.update { it.copy(error = "La contraseña debe tener al menos 8 caracteres") }
            return
        }

        if (!state.isLoginMode && state.password != state.confirmPassword) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }

        // LLAMADA A LA API
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = if (state.isLoginMode) {
                authRepository.login(state.email, state.password)
            } else {
                authRepository.register(state.email, state.password)
            }

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isLoading = false, error = exception.message ?: "Error de conexión")
                }
            }
        }
    }

    // Función para limpiar el estado de éxito una vez que navegamos
    fun resetSuccessState() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}