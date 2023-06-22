package com.libranta.pokedex.data.remote


import com.libranta.pokedex.data.remote.catalog.Pokemon
import com.libranta.pokedex.data.remote.catalog.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonListResponse

    @GET("pokemon/{idOrName}")
    suspend fun getPokemonByName(
        @Path("idOrName") idOrName: String
    ): Pokemon

}