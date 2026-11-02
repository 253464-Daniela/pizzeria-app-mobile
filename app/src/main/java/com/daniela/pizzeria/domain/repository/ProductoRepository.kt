package com.daniela.pizzeria.domain.repository

import com.daniela.pizzeria.domain.model.Producto

interface ProductoRepository {
    suspend fun getProductos(): Result<List<Producto>>
    suspend fun createProducto(producto: Producto): Result<Unit>
    suspend fun updateProducto(id: String, producto: Producto): Result<Unit>
    suspend fun deleteProducto(id: String): Result<Unit>
}