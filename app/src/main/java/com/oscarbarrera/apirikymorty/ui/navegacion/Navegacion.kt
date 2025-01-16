package com.oscarbarrera.apirikymorty.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oscarbarrera.apirikymorty.ui.screen.CharacterListScreen
import com.oscarbarrera.apirikymorty.ui.screen.CharacterScreen

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    //Rutas como cadenas de texto
    val listaPersonajesRoute = "lista_personajes"
    val personajeRoute = "personaje"

    NavHost(
        navController = navController,
        startDestination = listaPersonajesRoute
    ) {
        composable(listaPersonajesRoute) { backStatEntry ->
            CharacterListScreen(
                viewModel(),
                { navController.navigate(personajeRoute) }, // Navegar a personaje
                { navController.navigate(listaPersonajesRoute) } // Navegar a la lista de personajes
            )
        }
        composable(personajeRoute) { backStatEntry ->
            CharacterScreen(
                viewModel()
            ) { navController.navigate(listaPersonajesRoute) } // Pantalla del personaje
        }
    }
}
