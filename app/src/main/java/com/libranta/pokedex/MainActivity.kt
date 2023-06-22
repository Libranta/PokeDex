package com.libranta.pokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.libranta.pokedex.data.local.classes.BottomBar
import com.libranta.pokedex.data.remote.PokemonRepository
import com.libranta.pokedex.data.remote.catalog.PokemonMainData
import com.libranta.pokedex.data.shared.controllers.BackPressHandler
import com.libranta.pokedex.ui.items.fixed.DataCard
import com.libranta.pokedex.ui.navigation.screens.Screen
import com.libranta.pokedex.ui.navigation.screens.feed.FeedScreen
import com.libranta.pokedex.ui.navigation.screens.settings.SettingsScreen
import com.libranta.pokedex.ui.theme.PokeDexTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeDexTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenSet()
                }
            }
        }
    }
}
suspend fun fetchPokemonList(
    repository: PokemonRepository,
    offset: Int,
    limit: Int
): List<PokemonMainData> {
    return repository.getPokemonList(offset, limit).map { pokemon ->
        PokemonMainData(
            url = pokemon.url,
            name = pokemon.name,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreenSet(){

    //UI Values
    val cPrimary = MaterialTheme.colorScheme.primary
    val cOnPrimary = MaterialTheme.colorScheme.onPrimary
    val cSecondaryContainer = MaterialTheme.colorScheme.secondaryContainer
    val cSurface = MaterialTheme.colorScheme.surface
    val cOnSurface = MaterialTheme.colorScheme.onSurface
    val cOnSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val cSurfaceWithElevation = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    val navItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor =   cPrimary,
        unselectedIconColor = cOnSurfaceVariant,
        indicatorColor =      cSecondaryContainer,
        selectedTextColor =   cOnPrimary,
        unselectedTextColor = cOnSurfaceVariant,
    )
    val sLabelMedium = MaterialTheme.typography.labelMedium

    // UI Design and Needed Parameters
    cSurfaceWithElevation.let {
        rememberSystemUiController().apply {
            setNavigationBarColor(color = it)
            setStatusBarColor(color = it)
        }
    }
    val screens = listOf(
        BottomBar.Feed,
        BottomBar.Settings
    )
    val sFeed = stringResource(id = BottomBar.Feed.title)
    val sSettings = stringResource(id = BottomBar.Settings.title)

    //Behavior and Navigation
    val navController: NavHostController = rememberNavController()
    val defaultDestination : String = screens[0].route
    // State for the bottom sheet
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val selectedPokemon = remember { mutableStateOf<PokemonMainData?>(null) }

    // Fetch the Pokemon list using the repository
    val repository = PokemonRepository()
    val coroutineScope = rememberCoroutineScope()
    val pokemonList = remember { mutableStateListOf<PokemonMainData>() }
    val isInitialFetchCompleted = remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        if (!isInitialFetchCompleted.value) {
            coroutineScope.launch {
                val fetchedList = fetchPokemonList(repository,offset = 0, limit = 20)
                pokemonList.addAll(fetchedList)
                isInitialFetchCompleted.value = true
            }
        }
    }

    //Scaffold Structure - Experimental API annotated
    Scaffold(
        bottomBar ={
            NavigationBar(
                containerColor = cSurface,
                contentColor = cOnSurface,
                tonalElevation = 3.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        },
                        label = {
                            Text(
                                text = sFeed ,
                                style = sLabelMedium,
                                color = cOnSurfaceVariant,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = sSettings,
                                tint = cOnSurfaceVariant
                            )
                        },
                        alwaysShowLabel = false,
                        colors = navItemColors
                    )
                }

            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = defaultDestination
                ) {
                    composable(Screen.Feed.route) {
                        val isRefreshExecuted = remember { mutableStateOf(false) }

                        FeedScreen(
                            pokemonList = pokemonList,
                            onRefresh = {
                                if (!isRefreshExecuted.value) {
                                    coroutineScope.launch {
                                        pokemonList.clear()
                                        val fetchedList = fetchPokemonList(repository, offset = 0, limit = 20)
                                        pokemonList.addAll(fetchedList)
                                        isRefreshExecuted.value = true
                                    }
                                }
                            },
                            onScrolledToEnd = {
                                coroutineScope.launch {
                                    val offset = pokemonList.size
                                    val fetchedList = fetchPokemonList(repository, offset = offset, limit = 20)
                                    pokemonList.addAll(fetchedList)
                                }
                            },
                            onItemClick = { pokemon ->
                                selectedPokemon.value = PokemonMainData(
                                    name = pokemon.name,
                                    url = pokemon.url
                                )
                                CoroutineScope(Dispatchers.Main).launch {
                                    bottomSheetState.show()
                                }
                            }
                        )
                    }

                    composable(Screen.Settings.route) {
                        SettingsScreen()
                    }
                }

                // Full-screen card transition
                Crossfade(targetState = selectedPokemon.value) { pokemon ->
                    if (pokemon != null) {
                        AnimatedVisibility(visible = true) {
                            CardFullScreen(
                                pokemon = pokemon,
                                onClose = {
                                    selectedPokemon.value = null
                                },
                                repository = repository
                            )
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardFullScreen(
    pokemon: PokemonMainData,
    onClose: () -> Unit,
    repository: PokemonRepository
) {
    val swipeableState = rememberSwipeableState(0f)
    val scope = rememberCoroutineScope()
    val translateY = remember { Animatable(0f) }
    var isLoading by remember { mutableStateOf(true) }

    val pokemonId = pokemon.url.split("/").last { it.isNotEmpty() }.toInt()
    var pokemonName by remember { mutableStateOf(pokemon.name) }
    var pokemonHeight by remember { mutableStateOf("N/A") }
    var pokemonWeight by remember { mutableStateOf("N/A") }
    var pokemonorder by remember { mutableStateOf("N/A") }
    var pokemonBaseExp by remember { mutableStateOf("N/A") }
    var isDefault by remember { mutableStateOf(true) }

    val backgroundLottie by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_character_background))
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER ,
            value = SimpleColorFilter(MaterialTheme.colorScheme.primary.toArgb()),
            keyPath = arrayOf("Character", "Body", "Color")
        )
    )

    BackPressHandler {
        onClose()
    }
    LaunchedEffect(swipeableState.offset) {
        scope.launch {
            swipeableState.animateTo(
                targetValue = 0f,
                anim = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    LaunchedEffect(true) {
        val pokemonResponse = repository.getPokemonByName(pokemon.name)
        if (pokemonResponse != null) {
            pokemonName = pokemonResponse.name
            pokemonHeight = pokemonResponse.height.toString()
            pokemonWeight = pokemonResponse.weight.toString()
            pokemonorder = pokemonResponse.order.toString()
            pokemonBaseExp = pokemonResponse.base_experience.toString()
            isDefault = pokemonResponse.is_default
        }
        isLoading = false
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(0, translateY.value.roundToInt()) }
            .swipeable(
                state = swipeableState,
                anchors = mapOf(0f to 0f, -600f to -600f),
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical
            ),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        if (isLoading) {
            // Display a loading indicator while the data is being fetched
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                IconButton(
                    onClick = onClose
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .size(350.dp)
                    .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))){
                    LottieAnimation(
                        composition = backgroundLottie,
                        iterations = LottieConstants.IterateForever,
                        isPlaying = true,
                        dynamicProperties = dynamicProperties,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(300.dp)
                            .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .align(Alignment.Center)
                    )

                    Image(
                        painter = rememberGlidePainter(
                            request = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonId}.png",
                            fadeIn = true,
                            previewPlaceholder = R.drawable.ic_launcher_background
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .align(Alignment.Center)
                    )

                    }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(200.dp, 50.dp),
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pokemon.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }

                    DataCard(title = "Height", subtitle =pokemonHeight)
                    DataCard(title = "Weight", subtitle =pokemonWeight)
                    DataCard(title = "Order", subtitle =pokemonorder)
                    DataCard(title = "Base Experience", subtitle =pokemonBaseExp)
                    DataCard(title = "Is Default", subtitle =isDefault.toString())

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }

    }
}


@Preview(
    name = "Home Screen",
    showBackground = true,
    widthDp = 420,
)
@Composable
fun HomeScreenPreview(){
    HomeScreenSet()
}
