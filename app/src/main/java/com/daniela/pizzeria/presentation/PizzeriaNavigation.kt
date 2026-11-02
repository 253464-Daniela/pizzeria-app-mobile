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
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun PizzeriaNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as PizzeriaApplication
    val scope = rememberCoroutineScope() // Para el logout

    val startDestination = remember {
        if (app.container.authRepository.isSessionActive()) "home" else "auth"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("home") { popUpTo("auth") { inclusive = true } }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onLogout = {
                    scope.launch {
                        app.container.authRepository.logout()
                        navController.navigate("auth") { popUpTo("home") { inclusive = true } }
                    }
                },
                onProductoClick = { id -> /* navController.navigate("detalle/$id") - Lo haremos luego */ },
                onAddClick = { /* navController.navigate("formulario") - Lo haremos luego */ }
            )
        }
    }
}