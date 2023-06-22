package com.libranta.pokedex.ui.navigation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.libranta.pokedex.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity(){
    override fun onCreate(savedInstance : Bundle?){
        super.onCreate(savedInstance)

        //Sound When Its opened
        //TODO: Implement MediaPlayer

        lifecycleScope.launchWhenCreated {
            //TODO: Stop MediaPlayer by Listener
            //If conditions with pending verification exist like triggers, implement here before startActivity
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }

    }

}