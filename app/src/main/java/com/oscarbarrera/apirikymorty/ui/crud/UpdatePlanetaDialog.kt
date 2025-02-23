package com.oscarbarrera.apirikymorty.ui.crud

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.oscarbarrera.apirikymorty.model.Characters
import com.oscarbarrera.apirikymorty.model.PlanetaProcedencia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePlanetaDialog(
    planeta: PlanetaProcedencia,
    onPlanetaUpdated: (PlanetaProcedencia) -> Unit,
    onDialogDismissed: () -> Unit
) {
    var nombre by remember { mutableStateOf(planeta.nombre) }
    var descripcion by remember { mutableStateOf(planeta.descripcion) }
    var temperaturaMedia by remember { mutableDoubleStateOf(planeta.temperaturaMedia!!) }


    AlertDialog(
        title = { Text(text = "Actualizar planeta") },
        onDismissRequest = { onDialogDismissed() },
        confirmButton = {
            Button(
                onClick = {
                    val newPlaneta = PlanetaProcedencia(
                        id = planeta.id,
                        userId = planeta.userId,
                        personajeId = planeta.personajeId,
                        nombre = nombre,
                        descripcion = descripcion,
                        temperaturaMedia = temperaturaMedia
                    )
                    onPlanetaUpdated(newPlaneta)
                    nombre = ""
                    descripcion = ""
                    temperaturaMedia = 0.00
                }
            ) {
                Text(text = "Actualizar")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDialogDismissed() }
            ) {
                Text(text = "Cancelar")
            }
        },
        text = {
            Column() {
                TextField(
                    value = nombre ?: "",
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del planeta") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                descripcion?.let {
                    TextField(
                        value = it,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripcion del planeta") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType =
                            KeyboardType.Text
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = temperaturaMedia.toString(),
                    onValueChange = { temperaturaMedia = it.toDoubleOrNull() ?: 0.00 },
                    label = { Text("Temperatura media del planeta") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
            }
        }
    )
}