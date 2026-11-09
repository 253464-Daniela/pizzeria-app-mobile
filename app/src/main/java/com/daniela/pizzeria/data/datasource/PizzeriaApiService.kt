package com.daniela.pizzeria.data.datasource

import com.daniela.pizzeria.data.dto.AuthResponseDto
import com.daniela.pizzeria.data.dto.IngredienteDto
import com.daniela.pizzeria.data.dto.ProductoCreateDto
import com.daniela.pizzeria.data.dto.ProductoDto
import com.daniela.pizzeria.data.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getProductos(@Query("solo_disponibles") soloDisponibles: Boolean = false): List<ProductoDto>

    @POST("productos")
    suspend fun createProducto(@Body producto: ProductoCreateDto): ProductoDto

    @PUT("productos/{id}")
    suspend fun updateProducto(@Path("id") id: String, @Body producto: ProductoCreateDto): ProductoDto

    @DELETE("productos/{id}")
    suspend fun deleteProducto(@Path("id") id: String)

    @GET("productos/ingredientes")
    suspend fun getIngredientes(): List<IngredienteDto>

}