package com.daniela.pizzeria.domain.model

import com.daniela.pizzeria.data.remote.dto.ProductoDto

fun ProductoDto.toDomain(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion ?: "",
        precio = this.precio,
        disponible = this.disponible,
        categoriaId = this.categoriaId
    )
}