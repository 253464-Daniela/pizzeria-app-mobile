package com.daniela.pizzeria.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniela.pizzeria.data.repository.ProductoRepository
import com.daniela.pizzeria.presentation.screens.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        cargarProductos() // Carga inicial al abrir la pantalla
    }

    fun cargarProductos(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { it.copy(isRefreshing = true) }
            } else {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }

            val result = productoRepository.getProductos()

            result.onSuccess { lista ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false, // <-- Apagamos el refresco
                        productos = lista,
                        error = null
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false, // <-- Apagamos el refresco
                        error = exception.message ?: "Error al cargar productos"
                    )
                }
            }
        }
    }

    fun eliminarProducto(id: String) {
        viewModelScope.launch {
            val result = productoRepository.deleteProducto(id)
            result.onSuccess {
                cargarProductos() // Recargamos la lista para que desaparezca el eliminado
            }.onFailure { exception ->
                _uiState.update { it.copy(error = "Error al eliminar: ${exception.message}") }
            }
        }
    }
}