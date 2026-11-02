package com.daniela.pizzeria.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daniela.pizzeria.PizzeriaApplication
import com.daniela.pizzeria.presentation.auth.AuthScreen
import com.daniela.pizzeria.presentation.home.HomeScreen
import kotlinx.coroutines.launch

@Composable
fun PizzeriaNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as PizzeriaApplication

    // Verificamos si ya hay una sesión activa (DataStore)
    val startDestination = remember {
        if (app.container.authRepository.isSessionActive()) "home" else "auth"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("home") {
                        // Borra el historial para que no se pueda volver con Atrás
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onLogout = {
                    // Hacemos el logout de forma asíncrona usando una corrutina simple
                    kotlinx.coroutines.GlobalScope.launch {
                        app.container.authRepository.logout()
                        navController.navigate("auth") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}