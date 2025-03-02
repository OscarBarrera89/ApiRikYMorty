package com.oscarbarrera.apirikymorty.ui.screen.screenPrincipal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.oscarbarrera.apirikymorty.R
import com.oscarbarrera.apirikymorty.ui.crud.AddCharacterDialog
import com.oscarbarrera.apirikymorty.ui.crud.DeleteCharacterDialog
import com.oscarbarrera.apirikymorty.ui.crud.UpdateCharacterDialog
import com.oscarbarrera.apirikymorty.data.AuthManager
import com.oscarbarrera.apirikymorty.data.FirestoreManager
import com.oscarbarrera.apirikymorty.model.Characters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPrincipal(
    auth: AuthManager,
    firestore: FirestoreManager,
    navigateToLogin: () -> Unit,
    navigateToDetalle: (String) -> Unit
) {
    val user = auth.getCurrentUser()
    val factory = PrincipalViewModelFactory(firestore)
    val principalViewModel = viewModel(PrincipalViewModel::class.java, factory = factory)
    val uiState by principalViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = user?.displayName ?: "Anónimo",
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = user?.email ?: "Sin correo",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(
                        ContextCompat.getColor(
                            LocalContext.current,
                            R.color.purple_500
                        )
                    )
                ),
                actions = {
                    IconButton(onClick = {
                        principalViewModel.onLogoutSelected()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { principalViewModel.onAddCharacterSelected() },
                containerColor = Color.Gray
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir personaje")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Lista de personajes",  style = TextStyle(fontSize = 24.sp))
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (uiState.showLogoutDialog) {
                LogoutDialogP(
                    onDismiss = { principalViewModel.dismisShowLogoutDialog() },
                    onConfirm = {
                        auth.signOut()
                        navigateToLogin()
                        principalViewModel.dismisShowLogoutDialog()
                    }
                )
            }

            if (uiState.showAddCharacterDialog) {
                AddCharacterDialog(
                    onCharacterAdded = { personaje ->
                        principalViewModel.addCharacter(
                            Characters(
                                id = "",
                                userId = auth.getCurrentUser()?.uid,
                                personaje.oficio,
                                personaje.gender,
                                personaje.name,
                                personaje.age,
                                personaje.species,
                                personaje.status,
                            )

                        )
                        principalViewModel.dismisShowAddCharacterDialog()
                    },
                    onDialogDismissed = { principalViewModel.dismisShowAddCharacterDialog() },
                    auth
                )
            }

            if (!uiState.personajes.isNullOrEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    items(uiState.personajes) { personaje ->
                        CharacterItem(
                            personaje = personaje,
                            deleteCharacter = {
                                principalViewModel.deleteCharacterById(
                                    personaje.id ?: ""
                                )
                            },
                            updateCharacter = {
                                principalViewModel.updateCharacter(it)
                            },
                            navigateToDetalle = { personaje.id?.let { it1 -> navigateToDetalle(it1) } }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay datos")
                }
            }
        }
    }


}

@Composable
fun CharacterItem(
    personaje: Characters,
    deleteCharacter: () -> Unit,
    updateCharacter: (Characters) -> Unit,
    navigateToDetalle: (String) -> Unit
) {
    var showDeleteCharacterDialog by remember { mutableStateOf(false) }
    var showUpdateCharacterDialog by remember { mutableStateOf(false) }

    if (showDeleteCharacterDialog) {
        DeleteCharacterDialog(
            onConfirmDelete = {
                deleteCharacter()
                showDeleteCharacterDialog = false
            },
            onDismiss = { showDeleteCharacterDialog = false }
        )
    }

    if (showUpdateCharacterDialog) {
        UpdateCharacterDialog(
            personaje = personaje,
            onCharacterUpdated = { personaje ->
                updateCharacter(personaje)
                showUpdateCharacterDialog = false
            },
            onDialogDismissed = { showUpdateCharacterDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { personaje.id?.let { navigateToDetalle(it) } },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            personaje.name?.let { Text(text = it, style = MaterialTheme.typography.titleMedium) }
            Spacer(modifier = Modifier.height(4.dp))

            Column {
                Text(text = "Especie: ${personaje.species}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Estado: ${personaje.status}", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Función para determinar el color de la barra según el valor, dependiendo de la edad va a mejor o peor
            fun getStatColor(value: Int): Color {
                return when {
                    value <= 10 -> Color(0xFFE0F7FA) // Azul muy claro para edades jovenes
                    value in 11..20 -> Color(0xFF80D8FF) // Celeste claro
                    value in 21..30 -> Color(0xFF00AAFF) // Azul celeste
                    value in 31..40 -> Color.Cyan
                    value in 41..50 -> Color(0xFF00BFA5) // Verde azulado
                    value in 51..60 -> Color.Green
                    value in 61..70 -> Color(0xFFBFFF00) // Amarillo verdoso
                    value in 71..80 -> Color.Yellow
                    value in 81..90 -> Color(0xFFFFA500) // Naranja estándar
                    value in 91..100 -> Color(0xFFFF6600) // Naranja oscuro
                    else -> Color.Red // Si es mas de 100 está en rojo por critico o es un extraterrestre
                }
            }

            // Función para mostrar barra de progreso con color dinámico
            @Composable
            fun StatBar(label: String, value: Int, maxValue: Int = 255) {
                Column {
                    Text(text = "$label: $value", style = MaterialTheme.typography.bodySmall)
                    LinearProgressIndicator(
                        progress = { value / maxValue.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(MaterialTheme.shapes.small),
                        color = getStatColor(value), // Color dinámico según el valor
                        trackColor = Color(0xFFBDBDBD),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            personaje.age?.let { StatBar("Edad", it) }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { showUpdateCharacterDialog = true }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { showDeleteCharacterDialog = true }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}


@Composable
fun LogoutDialogP(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar Sesión") },
        text = {
            Text("¿Estás seguro de que deseas cerrar sesión?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}