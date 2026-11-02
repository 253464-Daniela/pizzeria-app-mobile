package com.daniela.pizzeria.domain.model

data class Ingrediente(
    val id: String,
    val nombre: String,
    val precioExtra: Double
): java.io.Serializable

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponible: Boolean,
    val categoriaId: String,
    val ingredientes: List<Ingrediente> = emptyList()
): java.io.Serializable