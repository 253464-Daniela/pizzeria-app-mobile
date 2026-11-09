package com.daniela.pizzeria.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredienteDto(
    val id: String,
    val nombre: String,
    @SerialName("precio_extra") val precioExtra: Double
)

@Serializable
data class ProductoDto(
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    val disponible: Boolean,
    @SerialName("categoria_id") val categoriaId: String,
    val ingredientes: List<IngredienteDto> = emptyList()
)

@Serializable
data class ProductoCreateDto(
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    @SerialName("categoria_id") val categoriaId: String,
    val disponible: Boolean = true,
    val ingredientes: List<String> = emptyList()
)