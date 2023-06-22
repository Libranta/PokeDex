package com.libranta.pokedex.ui.items.fixed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.libranta.pokedex.data.remote.catalog.Pokemon
import com.google.accompanist.glide.rememberGlidePainter
import com.libranta.pokedex.R
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign


@Composable
fun PokeCard(pokemon: Pokemon, onPokemonClick: (Pokemon) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onPokemonClick(pokemon) }
            .animateContentSize(),
        elevation = 4.dp,
        border = null,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberGlidePainter(
                    request = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
                    fadeIn = true,
                    previewPlaceholder = R.drawable.ic_launcher_background
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun PokemonCardPreview() {
    PokeCard(
        pokemon = Pokemon(
            id = 1,
            name = "Steven",
            height = 0,
            weight = 0,
            order = 0,
            base_experience = 0,
            is_default = false
        ),
        onPokemonClick = {

        }
    )
}


