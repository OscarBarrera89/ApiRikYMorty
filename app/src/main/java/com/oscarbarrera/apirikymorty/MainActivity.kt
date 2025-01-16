package com.oscarbarrera.apirikymorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.oscarbarrera.apirikymorty.ui.navegacion.Navegacion
import com.oscarbarrera.apirikymorty.ui.theme.ApiRikYMortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApiRikYMortyTheme {
                Navegacion()
            }
        }
    }
}


