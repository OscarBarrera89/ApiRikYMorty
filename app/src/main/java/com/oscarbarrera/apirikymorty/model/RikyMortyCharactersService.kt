package com.oscarbarrera.apirikymorty.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RikyMortyCharactersService {
    @GET("character")
    fun listCharacters(
        @Query("page") page: Int,
    ): Call<RikYMortyCharactersResult>
}
