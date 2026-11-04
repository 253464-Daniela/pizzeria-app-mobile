package com.daniela.pizzeria.data.repository

import com.daniela.pizzeria.data.model.Ingrediente
import com.daniela.pizzeria.data.model.Producto

interface ProductoRepository {
    suspend fun getProductos(): Result<List<Producto>>
    suspend fun createProducto(producto: Producto): Result<Unit>
    suspend fun updateProducto(id: String, producto: Producto): Result<Unit>
    suspend fun deleteProducto(id: String): Result<Unit>
    suspend fun getIngredientes(): Result<List<Ingrediente>>
}