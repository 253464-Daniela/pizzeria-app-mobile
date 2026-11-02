package com.daniela.pizzeria.data.repository

import com.daniela.pizzeria.data.local.SessionManager
import com.daniela.pizzeria.data.remote.api.ApiService
import com.daniela.pizzeria.data.remote.dto.RegisterRequestDto
import com.daniela.pizzeria.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val api: ApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.login(email, password)
            sessionManager.saveToken(response.accessToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.register(RegisterRequestDto(email, password))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override fun isSessionActive(): Boolean {
        return sessionManager.getTokenBlocking() != null
    }
}