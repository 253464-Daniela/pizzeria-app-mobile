package com.daniela.pizzeria.di

import android.content.Context
import com.daniela.pizzeria.data.datasource.ApiService
import com.daniela.pizzeria.data.local.SessionManager
import com.daniela.pizzeria.data.repository.AuthRepository
import com.daniela.pizzeria.data.repository.AuthRepositoryImpl
import com.daniela.pizzeria.data.repository.ProductoRepository
import com.daniela.pizzeria.data.repository.ProductoRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {
    private val baseUrl = "http://ec2-18-214-199-193.compute-1.amazonaws.com/api/v1/"

    private val json = Json { ignoreUnknownKeys = true }
    val sessionManager = SessionManager(context)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val token = sessionManager.getTokenBlocking()
            val request = chain.request().newBuilder()
            if (token != null) {
                request.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(request.build())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    val authRepository: AuthRepository = AuthRepositoryImpl(apiService, sessionManager)
    val productoRepository: ProductoRepository = ProductoRepositoryImpl(apiService)
}