package com.oscarbarrera.apirikymorty.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.oscarbarrera.apirikymorty.model.Characters
import com.oscarbarrera.apirikymorty.model.RickYMortyViewModel


// Composable que muestra la lista de personajes
@Composable
fun CharacterListScreen(viewModel: RickYMortyViewModel = viewModel(), navigateToPersonaje:()-> Unit, navigateToLista:() -> Unit) {
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
                CharacterItem(character = character, navigateToPersonaje)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Composable que representa un personaje individual
@Composable
fun CharacterItem(character: Characters, navigateToPersonaje: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{
                navigateToPersonaje()
            },
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