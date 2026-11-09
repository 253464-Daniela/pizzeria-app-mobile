package com.daniela.pizzeria.data.repository

import com.daniela.pizzeria.data.datasource.ApiService
import com.daniela.pizzeria.data.dto.ProductoCreateDto
import com.daniela.pizzeria.data.model.Ingrediente
import com.daniela.pizzeria.data.model.Producto
import com.daniela.pizzeria.data.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductoRepositoryImpl(private val api: ApiService) : ProductoRepository {

    override suspend fun getProductos(): Result<List<Producto>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getProductos(soloDisponibles = false)
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createProducto(producto: Producto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val dto = ProductoCreateDto(
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio,
                categoriaId = producto.categoriaId,
                disponible = producto.disponible,
                ingredientes = producto.ingredientes.map { it.id }
            )
            api.createProducto(dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProducto(id: String, producto: Producto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val dto = ProductoCreateDto(
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio,
                categoriaId = producto.categoriaId,
                disponible = producto.disponible,
                ingredientes = producto.ingredientes.map { it.id }
            )
            api.updateProducto(id, dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProducto(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.deleteProducto(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getIngredientes(): Result<List<Ingrediente>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getIngredientes()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}