package com.daniela.pizzeria.presentation.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniela.pizzeria.presentation.AppViewModelProvider
import androidx.compose.runtime.LaunchedEffect
import com.daniela.pizzeria.domain.model.Producto


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
            TopAppBar(title = { Text(if (uiState.isEditMode) "Editar Producto" else "Nuevo Producto") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
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