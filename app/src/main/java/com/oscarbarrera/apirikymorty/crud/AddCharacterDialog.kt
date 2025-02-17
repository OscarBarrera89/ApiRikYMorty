package com.oscarbarrera.apirikymorty.crud

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oscarbarrera.apirikymorty.data.AuthManager
import com.oscarbarrera.apirikymorty.model.Characters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCharacterDialog(
    onCharacterAdded: (Characters) -> Unit,
    onDialogDismissed: () -> Unit,
    auth: AuthManager
) {

    var name by remember { mutableStateOf("") }
    var oficio by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var age by remember { mutableIntStateOf(0) }

    val estadoPersonaje = listOf(
        "Vivo", "Muerto", "En Busca y Captura", "Revivido", "Desconocido"
    )

    val especiesPersonaje = listOf(
        "Riks", "Mortys", "Terrestre", "Letra", "Numero", "Desconocido"
    )

    var expandedEspecie by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }

    AlertDialog(
        title = { Text("Añadir personaje") },
        onDismissRequest = { onDialogDismissed() },
        confirmButton = {
            Button(
                onClick = {
                    val newPersonaje = Characters(
                        userId = auth.getCurrentUser()?.uid,
                        name = name,
                        oficio = oficio,
                        gender = gender,
                        species = species,
                        status = status,
                        age = age
                    )
                    onCharacterAdded(newPersonaje)
                    name = ""
                    oficio = ""
                    gender = ""
                    species = ""
                    status = ""
                    age = 0
                }
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDialogDismissed() }
            ) {
                Text("Cancelar")
            }
        },
        text = {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Dropdown Especies
                ExposedDropdownMenuBox(
                    expanded = expandedEspecie,
                    onExpandedChange = { expandedEspecie = it }
                ) {
                    TextField(
                        value = species,
                        onValueChange = {},
                        label = { Text("Especie") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (expandedEspecie) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                                contentDescription = "Desplegar lista"
                            )
                        },modifier = Modifier.menuAnchor() // Necesario al 100% porque si no no funciona el dorpdown
                    )
                    DropdownMenu(
                        expanded = expandedEspecie,
                        onDismissRequest = { expandedEspecie = false }
                    ) {
                        especiesPersonaje.forEach { especie ->
                            DropdownMenuItem(
                                text = { Text(especie, fontSize = 14.sp) },
                                onClick = {
                                    species = especie
                                    expandedEspecie = false
                                }
                            )
                        }
                    }
                }

                // Dropdown Estados
                ExposedDropdownMenuBox(
                    expanded = expandedEstado,
                    onExpandedChange = { expandedEstado = it }
                ) {
                    TextField(
                        value = status,
                        onValueChange = {},
                        label = { Text("Estado") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (expandedEstado) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                                contentDescription = "Desplegar lista"
                            )
                        },
                        modifier = Modifier.menuAnchor() // Necesario al 100% porque si no no funciona el dorpdown
                    )
                    DropdownMenu(
                        expanded = expandedEstado,
                        onDismissRequest = { expandedEstado = false }
                    ) {
                        estadoPersonaje.forEach { estado ->
                            DropdownMenuItem(
                                text = { Text(estado, fontSize = 14.sp) },
                                onClick = {
                                    status = estado
                                    expandedEstado  = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = oficio,
                    onValueChange = { oficio = it},
                    label = { Text("Oficio") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = age.toString(),
                    onValueChange = { age = it.toIntOrNull() ?: 1 },
                    label = { Text("Edad") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = gender,
                    onValueChange = { gender = it},
                    label = { Text("Género") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Text
                    )
                )

            }


        }

    )

}