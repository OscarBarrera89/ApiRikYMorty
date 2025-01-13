package com.oscarbarrera.apirikymorty.model

import retrofit2.http.GET
import retrofit2.http.Query

interface RikyMortyCharactersService {
    @GET("character/")
    suspend fun listCharacters(@Query("page") page: Int): RikYMortyCharactersResult

}
