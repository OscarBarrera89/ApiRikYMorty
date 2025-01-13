package com.oscarbarrera.apirikymorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.oscarbarrera.apirikymorty.model.Characters
import com.oscarbarrera.apirikymorty.ui.theme.ApiRikYMortyTheme
import com.oscarbarrera.apirikymorty.model.RickYMortyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApiRikYMortyTheme {
                CharacterListScreen()
            }
        }
    }
}

// Composable que muestra la lista de personajes
@Composable
fun CharacterListScreen(viewModel: RickYMortyViewModel = viewModel()) {
    // Obtenemos los datos desde el ViewModel usando StateFlow
    val characters by viewModel.characterList.collectAsState()
    val isLoading = characters.isEmpty()

    // UI content
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            items(characters) { character ->
                CharacterItem(character = character)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Composable que representa un personaje individual
@Composable
fun CharacterItem(character: Characters) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = character.name, style = MaterialTheme.typography.titleLarge)
                Text(text = "Species: ${character.species}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Status: ${character.status}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Location: ${character.location.name ?: "Unknown"}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

