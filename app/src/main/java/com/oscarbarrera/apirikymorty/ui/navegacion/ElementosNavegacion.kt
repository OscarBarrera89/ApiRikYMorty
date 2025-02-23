package com.oscarbarrera.apirikymorty.ui.navegacion

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object SignUp

@Serializable
object ForgotPassword

@Serializable
object ListaPersonajes

@Serializable
object DetallePersonajes

@Serializable
object CrudPersonajes

@Serializable
data class DetPersonajes(val id: String)
