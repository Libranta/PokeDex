package com.libranta.pokedex.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import com.libranta.pokedex.data.remote.catalog.Pokemon
import com.libranta.pokedex.data.remote.catalog.PokemonMainData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonRepository {
    private val apiService: PokemonApiService = createApiService()

    suspend fun getPokemonList(offset: Int, limit: Int): List<PokemonMainData> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonList(offset, limit)
                if (response.results.isNotEmpty()) {
                    response.results
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun getPokemonByName(name: String): Pokemon? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonByName(name)
                response
            } catch (e: Exception) {
                null
            }
        }
    }



    private fun createApiService(): PokemonApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(PokemonApiService::class.java)
    }



    companion object {
        suspend fun fetchPokemonList(repository: PokemonRepository, offset: Int, limit: Int): List<PokemonMainData> {
            return repository.getPokemonList(offset, limit)
        }
    }
}
