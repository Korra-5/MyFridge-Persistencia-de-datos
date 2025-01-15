package com.example.myfridge.Screens

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfridge.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun Menu(context: Context) {
    //Actualiza y inicializa el gestor de dataStore
    val dataStoreManager = remember { DataStoreManager(context) }

    //Inicia una lista de productos la cual se actualiza con la variabe flow
    val products by dataStoreManager.productsFlow.collectAsState(initial = emptyList())
    var showDialogAdd by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {


        Column {

            Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                Text(
                    text = "Productos en tu nevera:",
                    Modifier
                        .align(Alignment.Start)
                        .padding(12.dp),
                    color = Color.Black,
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                )

                //Cada producto invoca la funcion ProductCart
                products.forEach { product ->
                    ProductCard(dataStoreManager,
                        product = product,
                        //Funcion unitaria encargada de borrar, esta crea una lita nueva borrando el producto especificado y la guarda
                        onDelete = { toDelete ->
                            val updatedProducts = products - toDelete

                            //Crea una tarea en segundo plano que guarda los datos
                            CoroutineScope(Dispatchers.IO).launch {
                                dataStoreManager.saveProducts(updatedProducts)
                            }
                        }
                    )
                }
            }
        }

        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Icon",
                tint = Color.Black,
                modifier = Modifier.clickable {
                    showDialogAdd = true
                }
            )
        }

        if (showDialogAdd) {
            //Dialog encargado de añadir productos
            AddProduct(
                onDismiss = { showDialogAdd = false },
                //Funcion unitaria encargada de añadir le producto
                onAdd = { product ->
                    val updatedProducts = products + product
                    CoroutineScope(Dispatchers.IO).launch {
                        dataStoreManager.saveProducts(updatedProducts)
                    }
                }
            )
        }



    }
}


@Composable
fun AddProduct(
    onDismiss: () -> Unit,
    onAdd: (Product) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Agregar Producto") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Cantidad") }
                )
                OutlinedTextField(
                    value = notas,
                    onValueChange = { notas = it },
                    label = { Text("Notas del producto") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val product = Product(name, quantity.toIntOrNull() ?: 0, notas)
                    onAdd(product)
                    onDismiss()
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

 
@Composable
fun ProductCard(
    dataStoreManager: DataStoreManager,
    product: Product,
    onDelete: (Product) -> Unit
) {
    var showDialogEdit by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    val normalHeight = 105.dp

    //Esta variable y normalHeight son las encargadas de permitir aumentar el tamaño de la carta para verla mejor
    val cardHeight by animateDpAsState(
        targetValue = if (isExpanded) Dp.Unspecified else normalHeight
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .heightIn(min = normalHeight)
            .height(cardHeight)
            .clickable {
                if (isExpanded == true) {
                    isExpanded = false
                } else {
                    isExpanded = true
                }
            }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información del producto
            Column(Modifier.weight(1f)) {
                Text(
                    text = product.nombre,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Text(text = "Cantidad: ${product.cantidad}")
                Text(text = product.notas)
            }

            // Ícono de editar (No funciona por momentos)
            IconButton(onClick = {
                showDialogEdit = true
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Eliminar producto",
                    tint = Color.Green
                )
            }

            // Ícono de eliminar
            IconButton(onClick = { onDelete(product) }) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Eliminar producto",
                    tint = Color.Red
                )
            }
        }
    }
}
