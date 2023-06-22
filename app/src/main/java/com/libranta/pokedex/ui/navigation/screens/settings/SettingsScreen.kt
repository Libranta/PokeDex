package com.libranta.pokedex.ui.navigation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.libranta.pokedex.R

@Composable
fun SettingsScreen() {

    val loginLottie by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_pokeball))

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            LottieAnimation(
                composition = loginLottie,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight / 4),
                iterations = LottieConstants.IterateForever,
                speed = 1f,
                clipSpec = LottieClipSpec.Progress(0f,1f),
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Ups, something went wrong!",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = MaterialTheme.typography.headlineLarge.fontFamily,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This function is not available yet, please wait for the next update, well I really hope there is a next update :D",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                textAlign = TextAlign.Center
            )
        }
    }
}