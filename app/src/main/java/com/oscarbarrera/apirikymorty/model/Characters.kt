package com.oscarbarrera.apirikymorty.model

data class Characters(
    val id: String? = null,
    val userId: String?,
    val oficio: String,
    val gender: String,
    val name: String,
    val age: Int,
    val species: String,
    val status: String,
)