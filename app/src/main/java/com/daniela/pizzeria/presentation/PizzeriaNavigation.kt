package com.daniela.pizzeria.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daniela.pizzeria.PizzeriaApplication
import com.daniela.pizzeria.data.model.Producto
import com.daniela.pizzeria.presentation.auth.AuthScreen
import com.daniela.pizzeria.presentation.detail.ProductoDetailScreen
import com.daniela.pizzeria.presentation.form.ProductoFormScreen
import com.daniela.pizzeria.presentation.home.HomeScreen
import kotlinx.coroutines.launch

@Composable
fun PizzeriaNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as PizzeriaApplication
    val scope = rememberCoroutineScope()

    // Verificamos si ya hay una sesión activa (DataStore)
    val startDestination = remember {
        if (app.container.authRepository.isSessionActive()) "home" else "auth"
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            // HomeScreen crea su propio ViewModel por defecto gracias a su parámetro viewModel = ...
            HomeScreen(
                onLogout = {
                    scope.launch {
                        app.container.authRepository.logout()
                        navController.navigate("auth") { popUpTo("home") { inclusive = true } }
                    }
                },
                onProductoClick = { producto ->
                    // Guardamos el objeto Producto en la memoria de navegación
                    navController.currentBackStackEntry?.savedStateHandle?.set("producto", producto)
                    navController.navigate("detalle")
                },
                onAddClick = {
                    navController.navigate("formulario")
                }
            )
        }

        composable("detalle") {
            // Recuperamos el objeto Producto que guardamos al hacer clic
            val producto = navController.previousBackStackEntry?.savedStateHandle?.get<Producto>("producto")
            ProductoDetailScreen(
                producto = producto,
                onNavigateBack = { navController.popBackStack() },
                onEditClick = { prod ->
                    // Guardamos el producto para el formulario y navegamos
                    navController.currentBackStackEntry?.savedStateHandle?.set("producto_a_editar", prod)
                    navController.navigate("formulario")
                }
            )
        }

        composable("formulario") {
            val productoAEditar = navController.previousBackStackEntry?.savedStateHandle?.get<Producto>("producto_a_editar")
            ProductoFormScreen(
                productoAEditar = productoAEditar,
                onNavigateBack = {
                    if (productoAEditar != null) {
                        navController.popBackStack("home", inclusive = false)
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}