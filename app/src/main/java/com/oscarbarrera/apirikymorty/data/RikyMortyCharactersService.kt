package com.oscarbarrera.apirikymorty.data

import com.oscarbarrera.apirikymorty.model.RikYMortyCharactersResult
import retrofit2.http.GET
import retrofit2.http.Query

interface RikyMortyCharactersService {
    @GET("character/")
    suspend fun listCharacters(@Query("page") page: Int): RikYMortyCharactersResult

}
