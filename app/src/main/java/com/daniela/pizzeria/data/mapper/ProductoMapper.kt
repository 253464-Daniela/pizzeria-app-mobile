package com.daniela.pizzeria.data.mapper

import com.daniela.pizzeria.data.dto.IngredienteDto
import com.daniela.pizzeria.data.dto.ProductoDto
import com.daniela.pizzeria.data.model.Ingrediente
import com.daniela.pizzeria.data.model.Producto


fun IngredienteDto.toDomain(): Ingrediente {
    return Ingrediente(id, nombre, precioExtra)
}
fun ProductoDto.toDomain(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion ?: "",
        precio = this.precio,
        disponible = this.disponible,
        categoriaId = this.categoriaId,
        ingredientes = this.ingredientes.map { it.toDomain() }
    )
}