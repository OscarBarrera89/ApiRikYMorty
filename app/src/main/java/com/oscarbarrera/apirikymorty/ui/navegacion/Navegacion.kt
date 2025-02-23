package com.oscarbarrera.apirikymorty.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.oscarbarrera.apirikymorty.data.AuthManager
import com.oscarbarrera.apirikymorty.data.FirestoreManager
import com.oscarbarrera.apirikymorty.ui.screen.screenDetalle.ScreenPlanetas
import com.oscarbarrera.apirikymorty.ui.screen.screenFirebase.ForgotPasswordScreen
import com.oscarbarrera.apirikymorty.ui.screen.screenFirebase.LoginScreen
import com.oscarbarrera.apirikymorty.ui.screen.screenPrincipal.ScreenPrincipal
import com.pjurado.firebasecurso2425.screen.SignUpScreen

@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val firestore = FirestoreManager(auth, context)


    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                auth,
                { navController.navigate(SignUp) },
                {
                    navController.navigate(CrudPersonajes) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                { navController.navigate(ForgotPassword) }
            )
        }

        composable<SignUp> {
            SignUpScreen(
                auth
            ) { navController.popBackStack() }
        }

        composable <ForgotPassword> {
            ForgotPasswordScreen(
                auth
            ) { navController.navigate(Login) {
                popUpTo(Login){ inclusive = true }
            } }
        }
        composable<CrudPersonajes>{
            ScreenPrincipal(
                auth,
                firestore,
                { navController.navigate(Login) {
                    popUpTo(Login){ inclusive = true }
                }
                },
                { id ->
                    navController.navigate(DetPersonajes(id))
                }
            )
        }

        composable<DetPersonajes>{backStackEntry ->
            val detalle = backStackEntry.toRoute<DetPersonajes>()
            val id = detalle.id

            ScreenPlanetas(
                id,
                auth,
                firestore,
                { navController.navigate(Login) {
                    popUpTo(Login){ inclusive = true }
                }
                },
                { navController.navigate(CrudPersonajes) {
                    popUpTo(CrudPersonajes){ inclusive = true }
                }
                }
            )
        }

        //Parte de mostrar personajes de rik y morty de la api
        //Se puede borrar pero lo conservo
        /*composable<ListaPersonajes>{
            CharacterListScreen(
                auth,
                viewModel(),
                { navController.navigate(DetallePersonajes) },
                { navController.navigate(ListaPersonajes) },
                {
                    navController.navigate(Login) {
                        popUpTo(ListaPersonajes){ inclusive = true }
                    }
                }
            )
        }
        composable<DetallePersonajes>{
            CharacterScreen(
                auth,
                viewModel(),
                { navController.navigate(ListaPersonajes) }
            )
        }*/
    }
}
