package com.libranta.pokedex.data.remote.catalog

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val order: Int,
    val base_experience : Int,
    val is_default: Boolean,
)

