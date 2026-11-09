package com.daniela.pizzeria.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniela.pizzeria.presentation.viewmodels.AppViewModelProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import com.daniela.pizzeria.data.model.Producto
import com.daniela.pizzeria.presentation.viewmodels.ProductoFormViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    productoAEditar: Producto? = null,
    onNavigateBack: () -> Unit,
    viewModel: ProductoFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(productoAEditar) {
        if (productoAEditar != null) {
            viewModel.cargarProducto(productoAEditar)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (uiState.isEditMode) "Editar Producto" else "Nuevo Producto", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Para que no se corte en pantallas pequeñas
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.precio,
                onValueChange = { viewModel.onPrecioChange(it) },
                label = { Text("Precio ($)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Ingredientes disponibles:", style = MaterialTheme.typography.titleSmall)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.ingredientesDisponibles.forEach { ingrediente ->
                    val isSelected = uiState.ingredientesSeleccionados.contains(ingrediente.id)
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.toggleIngrediente(ingrediente.id) },
                        label = { Text(ingrediente.nombre) }
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.disponible,
                    onCheckedChange = { viewModel.onDisponibleChange(it) }
                )
                Text("Disponible")
            }



            if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = { viewModel.guardarProducto() },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}