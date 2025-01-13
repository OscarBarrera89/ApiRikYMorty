package com.oscarbarrera.apirikymorty.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RikYMortyCharactersClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(RikyMortyCharactersService::class.java)
}