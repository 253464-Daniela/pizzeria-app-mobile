package com.daniela.pizzeria.domain.model

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponible: Boolean,
    val categoriaId: String
)