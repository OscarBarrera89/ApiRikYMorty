package com.oscarbarrera.apirikymorty.ui.crud

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.oscarbarrera.apirikymorty.data.AuthManager
import com.oscarbarrera.apirikymorty.model.PlanetaProcedencia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlanetaDialog(
    onPlanetaAdded: (PlanetaProcedencia) -> Unit,
    onDialogDismissed: () -> Unit,
    auth: AuthManager
) {

    var personajeId by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var temperaturaMedia by remember { mutableDoubleStateOf(0.00) }


    AlertDialog(
        title = { Text("Añadir planeta de procedencia") },
        onDismissRequest = { onDialogDismissed() },
        confirmButton = {
            Button(
                onClick = {
                    val newPlaneta = PlanetaProcedencia(
                        personajeId = personajeId,
                        userId = auth.getCurrentUser()?.uid,
                        nombre = nombre,
                        descripcion = descripcion,
                        temperaturaMedia = temperaturaMedia
                    )
                    onPlanetaAdded(newPlaneta)
                    personajeId = ""
                    nombre = ""
                    descripcion = ""
                    temperaturaMedia = 0.00
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
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del planeta") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = descripcion,
                    onValueChange = { descripcion = it},
                    label = { Text("Descripcion del planeta") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = temperaturaMedia.toString(),
                    onValueChange = { temperaturaMedia = it.toDoubleOrNull() ?: 0.00 },
                    label = { Text("Temperatura Media") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
            }
        }

    )

}