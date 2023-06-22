package com.libranta.pokedex.ui.navigation.screens.feed


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import com.libranta.pokedex.data.remote.catalog.Pokemon
import com.libranta.pokedex.data.remote.catalog.PokemonMainData
import com.libranta.pokedex.ui.items.fixed.PokeCard

@Composable
fun FeedScreen(
    pokemonList: List<PokemonMainData>,
    onRefresh: () -> Unit,
    onScrolledToEnd: () -> Unit,
    onItemClick: (PokemonMainData) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val isRefreshing = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
            onRefresh = { onRefresh() }
        ) {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(pokemonList) { index, pokemon ->
                    PokeCard(
                        pokemon = Pokemon(
                            id = index + 1,
                            name = pokemon.name,
                            height = 0,
                            weight = 0,
                            order = 0,
                            base_experience = 0,
                            is_default = false
                        ),
                        onPokemonClick = {
                            onItemClick(pokemon)
                        }
                    )

                    // Trigger fetching next batch when reaching the end of the list
                    if (index == pokemonList.lastIndex && lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == index) {
                        onScrolledToEnd()

                    }
                }
            }
        }
    }
}
