package com.daniela.pizzeria.data.repository

import com.daniela.pizzeria.data.local.SessionManager
import com.daniela.pizzeria.data.remote.api.ApiService
import com.daniela.pizzeria.data.remote.dto.RegisterRequestDto
import com.daniela.pizzeria.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: ApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.login(email, password)
            sessionManager.saveToken(response.accessToken)
            Result.success(Unit)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Result.failure(Exception("Correo o contraseña incorrectos"))
            } else {
                Result.failure(Exception("Error del servidor: ${e.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión a internet"))
        }
    }

    override suspend fun register(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.register(RegisterRequestDto(email, password))
            sessionManager.saveToken(response.accessToken) // <-- GUARDAMOS EL TOKEN AQUÍ
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception("El correo ya está registrado o es inválido"))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión a internet"))
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override fun isSessionActive(): Boolean {
        return sessionManager.getTokenBlocking() != null
    }
}