package com.daniela.pizzeria.data.model

import java.io.Serializable

data class Ingrediente(
    val id: String,
    val nombre: String,
    val precioExtra: Double
): Serializable

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponible: Boolean,
    val categoriaId: String,
    val ingredientes: List<Ingrediente> = emptyList()
): Serializable