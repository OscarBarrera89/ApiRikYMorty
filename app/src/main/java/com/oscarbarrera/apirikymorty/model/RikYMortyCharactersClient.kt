package com.oscarbarrera.apirikymorty.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RikYMortyCharactersClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: RikyMortyCharactersService by lazy {
        retrofit.create(RikyMortyCharactersService::class.java)
    }
}