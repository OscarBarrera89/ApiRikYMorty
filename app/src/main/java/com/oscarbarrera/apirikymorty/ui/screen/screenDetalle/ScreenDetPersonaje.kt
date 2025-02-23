package com.oscarbarrera.apirikymorty.ui.screen.screenDetalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oscarbarrera.apirikymorty.R
import com.oscarbarrera.apirikymorty.data.AuthManager
import com.oscarbarrera.apirikymorty.data.FirestoreManager
import com.oscarbarrera.apirikymorty.model.PlanetaProcedencia
import com.oscarbarrera.apirikymorty.ui.crud.AddPlanetaDialog
import com.oscarbarrera.apirikymorty.ui.crud.DeletePlanetaDialog
import com.oscarbarrera.apirikymorty.ui.screen.screenPrincipal.PrincipalViewModel
import com.oscarbarrera.apirikymorty.ui.crud.UpdatePlanetaDialog
import com.oscarbarrera.apirikymorty.ui.screen.screenPrincipal.PrincipalViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPlanetas(
    idPersonaje: String,
    auth: AuthManager,
    firestore: FirestoreManager,
    navigateToLogin: () -> Unit,
    navigateToPrincipal: () -> Unit,
) {
    val user = auth.getCurrentUser()
    val factoryPrincipal = PrincipalViewModelFactory(firestore)
    val principalViewModel = viewModel(PrincipalViewModel::class.java, factory = factoryPrincipal)

    val factoryPlaneta = PlanetaViewModelFactory(firestore, idPersonaje)
    val planetaViewModel = viewModel(PlanetaViewModel::class.java, factory = factoryPlaneta)

    val personaje by principalViewModel.personaje.collectAsState()
    val uiStatePrincipal by principalViewModel.uiState.collectAsState()
    val uiStatePlaneta by planetaViewModel.uiState.collectAsState()

    LaunchedEffect(idPersonaje) {
        principalViewModel.getPersonajeById(idPersonaje)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            R.color.teal_700
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Botón de volver atrás (izquierda)
                FloatingActionButton(
                    onClick = { navigateToPrincipal() },
                    containerColor = Color.Gray,
                    modifier = Modifier.align(Alignment.BottomStart)
                        .padding(start = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                        contentDescription = "Volver"
                    )
                }

                // Botón de añadir planeta (derecha)
                FloatingActionButton(
                    onClick = { planetaViewModel.onAddPlanetaSelected() },
                    containerColor = Color.Gray,
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir planeta"
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Lista de planetas de ${personaje?.name}",  style = TextStyle(fontSize = 24.sp))
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (uiStatePrincipal.showLogoutDialog) {
                LogoutDialog(
                    onDismiss = { principalViewModel.dismisShowLogoutDialog() },
                    onConfirm = {
                        auth.signOut()
                        navigateToLogin()
                        principalViewModel.dismisShowLogoutDialog()
                    }
                )
            }

            if (uiStatePlaneta.showAddPlanetaDialog) {
                AddPlanetaDialog(
                    onPlanetaAdded = { planeta ->
                        planetaViewModel.addPlaneta(
                            PlanetaProcedencia(
                                id = "",
                                personajeId = personaje?.id,
                                userId = auth.getCurrentUser()?.uid,
                                planeta.nombre ?: "",
                                planeta.descripcion ?: "",
                                planeta.temperaturaMedia ?: 0.00,
                            )
                        )
                        planetaViewModel.dismisShowAddPlanetaDialog()
                    },
                    onDialogDismissed = { planetaViewModel.dismisShowAddPlanetaDialog() },
                    auth
                )
            }

            if (!uiStatePlaneta.planetas.isNullOrEmpty()) {


                LazyColumn(
                    modifier = Modifier.padding(top = 60.dp)
                ) {
                    items(uiStatePlaneta.planetas) { planeta ->
                        PlanetaItem(
                            planeta = planeta,
                            deletePlaneta = {
                                planetaViewModel.deletePlanetaById(
                                    planeta.id ?: ""
                                )
                            },
                            updatePlaneta = {
                                planetaViewModel.updatePlaneta(it)
                            }
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
fun PlanetaItem(
    planeta: PlanetaProcedencia,
    deletePlaneta: () -> Unit,
    updatePlaneta: (PlanetaProcedencia) -> Unit,
) {

    var showDeletePlanetaDialog by remember { mutableStateOf(false) }
    var showUpdatePlanetaDialog by remember { mutableStateOf(false) }

    if (showDeletePlanetaDialog) {
        DeletePlanetaDialog(
            onConfirmDelete = {
                deletePlaneta()
                showDeletePlanetaDialog = false
            },
            onDismiss = { showDeletePlanetaDialog = false }
        )
    }

    if (showUpdatePlanetaDialog) {
        UpdatePlanetaDialog(
            planeta = planeta,
            onPlanetaUpdated = { planeta ->
                updatePlaneta(planeta)
                showUpdatePlanetaDialog = false
            },
            onDialogDismissed = { showUpdatePlanetaDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)


    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column {
                planeta.nombre?.let { Text(text = it, style = MaterialTheme.typography.titleLarge) }
                Text(
                    text = "Descripcion: ${planeta.descripcion}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Temperatura media: ${planeta.temperaturaMedia}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(AbsoluteAlignment.Right)
        ) {
            IconButton(
                onClick = { showUpdatePlanetaDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Actualizar Planeta"
                )
            }
            IconButton(
                onClick = { showDeletePlanetaDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar Planeta"
                )
            }
        }
    }
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
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