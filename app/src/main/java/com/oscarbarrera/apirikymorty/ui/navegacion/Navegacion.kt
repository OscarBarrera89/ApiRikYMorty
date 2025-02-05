package com.oscarbarrera.apirikymorty.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oscarbarrera.apirikymorty.data.AuthManager
import com.oscarbarrera.apirikymorty.ui.screen.CharacterListScreen
import com.oscarbarrera.apirikymorty.ui.screen.CharacterScreen
import com.oscarbarrera.apirikymorty.ui.screen.ForgotPasswordScreen
import com.oscarbarrera.apirikymorty.ui.screen.LoginScreen
import com.pjurado.firebasecurso2425.screen.SignUpScreen

@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current


    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                auth,
                { navController.navigate(SignUp) },
                {
                    navController.navigate(ListaPersonajes) {
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
        composable<ListaPersonajes>{
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
        }
    }
}
