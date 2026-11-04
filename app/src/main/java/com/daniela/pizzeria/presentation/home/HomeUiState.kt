package com.daniela.pizzeria.presentation.home

import com.daniela.pizzeria.data.model.Producto

data class HomeUiState(
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)