package com.oscarbarrera.apirikymorty.model

data class RikYMortyCharactersResult(
    val info: Info,
    val results: List<Characters>
)