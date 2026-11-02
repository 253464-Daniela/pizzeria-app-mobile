package com.daniela.pizzeria.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daniela.pizzeria.domain.model.Producto
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetailScreen(
    producto: Producto?,
    onNavigateBack: () -> Unit,
    onEditClick: (Producto) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                actions = {
                    if (producto != null) {
                        IconButton(onClick = { onEditClick(producto) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (producto == null) {
            Text("Producto no encontrado", modifier = Modifier.padding(padding))
            return@Scaffold
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = producto.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = producto.descripcion, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Precio: $${producto.precio}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(text = "Ingredientes:", style = MaterialTheme.typography.titleMedium)
            if (producto.ingredientes.isEmpty()) {
                Text("Sin ingredientes extra", style = MaterialTheme.typography.bodyMedium)
            } else {
                producto.ingredientes.forEach { ing ->
                    Text("- ${ing.nombre} ($${ing.precioExtra})", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Text(text = if (producto.disponible) "Estado: Disponible" else "Estado: Agotado")
            Text(text = "ID de Categoría: ${producto.categoriaId}", style = MaterialTheme.typography.bodySmall)
        }
    }
}