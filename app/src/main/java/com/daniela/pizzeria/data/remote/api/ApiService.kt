package com.daniela.pizzeria.data.remote.api

import com.daniela.pizzeria.data.remote.dto.*
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @GET("productos")
    suspend fun getProductos(): List<ProductoDto>

    @POST("productos")
    suspend fun createProducto(@Body producto: ProductoCreateDto): ProductoDto

    @PUT("productos/{id}")
    suspend fun updateProducto(@Path("id") id: String, @Body producto: ProductoCreateDto): ProductoDto

    @DELETE("productos/{id}")
    suspend fun deleteProducto(@Path("id") id: String)

    @GET("productos/ingredientes")
    suspend fun getIngredientes(): List<IngredienteDto>

}