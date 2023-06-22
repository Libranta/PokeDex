package com.libranta.pokedex.data.remote.catalog

data class PokemonMainData (
    val name: String,
    val url: String
)

data class PokemonListResponse(
    val results: List<PokemonMainData>
)