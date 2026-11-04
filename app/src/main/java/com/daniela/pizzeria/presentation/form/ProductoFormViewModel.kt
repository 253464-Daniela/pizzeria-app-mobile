package com.daniela.pizzeria.presentation.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniela.pizzeria.data.model.Ingrediente
import com.daniela.pizzeria.data.model.Producto
import com.daniela.pizzeria.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormUiState(
    val id: String? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val categoriaId: String = "f32460c4-03ac-47dc-85a9-435dee8b75e6",
    val disponible: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isEditMode: Boolean = false,
    val ingredientesDisponibles: List<Ingrediente> = emptyList(),
    val ingredientesSeleccionados: Set<String> = emptySet()
)

class ProductoFormViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    init {
        cargarIngredientes()
    }

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    private fun cargarIngredientes() {
        viewModelScope.launch {
            val result = repository.getIngredientes()
            result.onSuccess { lista ->
                _uiState.update { it.copy(ingredientesDisponibles = lista) }
            }
        }
    }

    fun toggleIngrediente(id: String) {
        _uiState.update { state ->
            val seleccionados = state.ingredientesSeleccionados.toMutableSet()
            if (seleccionados.contains(id)) seleccionados.remove(id) else seleccionados.add(id)
            state.copy(ingredientesSeleccionados = seleccionados)
        }
    }
    fun onNombreChange(nombre: String) = _uiState.update { it.copy(nombre = nombre, error = null) }
    fun onDescripcionChange(desc: String) = _uiState.update { it.copy(descripcion = desc, error = null) }
    fun onPrecioChange(precio: String) = _uiState.update { it.copy(precio = precio, error = null) }
    fun onDisponibleChange(disponible: Boolean) = _uiState.update { it.copy(disponible = disponible) }

    fun guardarProducto() {
        val state = _uiState.value
        val precioDouble = state.precio.toDoubleOrNull()
        val ingredientesObj = state.ingredientesDisponibles.filter { state.ingredientesSeleccionados.contains(it.id) }

        // VALIDACIONES
        if (state.nombre.isBlank()) {
            _uiState.update { it.copy(error = "El nombre es obligatorio") }
            return
        }
        if (precioDouble == null || precioDouble <= 0) {
            _uiState.update { it.copy(error = "El precio debe ser mayor a 0") }
            return
        }

        val producto = Producto(
            id = state.id ?: "",
            nombre = state.nombre,
            descripcion = state.descripcion,
            precio = precioDouble,
            categoriaId = state.categoriaId,
            disponible = state.disponible,
            ingredientes = ingredientesObj
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = if (state.isEditMode) {
                repository.updateProducto(state.id!!, producto)
            } else {
                repository.createProducto(producto)
            }

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message ?: "Error al guardar") }
            }
        }
    }

    // Esta función se llama cuando navegamos a Editar
    fun cargarProducto(producto: Producto) {
        _uiState.update {
            it.copy(
                id = producto.id,
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio.toString(),
                categoriaId = producto.categoriaId,
                disponible = producto.disponible,
                isEditMode = true,
                ingredientesSeleccionados = producto.ingredientes.map { ing -> ing.id }.toSet()
            )
        }
    }
}