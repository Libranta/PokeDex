package com.libranta.pokedex.data.shared.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libranta.pokedex.data.remote.PokemonRepository
import com.libranta.pokedex.data.remote.catalog.PokemonMainData
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {
    private val repository = PokemonRepository()
    private val pokemonList = mutableListOf<PokemonMainData>()
    private var offset = 0

    fun getPokemonList(): List<PokemonMainData> {
        return pokemonList
    }

    fun fetchPokemonList() {
        if (offset < pokemonList.size) {
            // Already fetched this batch, return
            return
        }

        viewModelScope.launch {
            val batch = repository.getPokemonList(offset, BATCH_SIZE)
            pokemonList.addAll(batch)
            offset += BATCH_SIZE
        }
    }

    companion object {
        private const val BATCH_SIZE = 20
    }
}
