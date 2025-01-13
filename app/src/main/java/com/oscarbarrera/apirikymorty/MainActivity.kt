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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.oscarbarrera.apirikymorty.model.Characters
import com.oscarbarrera.apirikymorty.model.Location
import com.oscarbarrera.apirikymorty.model.Origin
import com.oscarbarrera.apirikymorty.model.RikYMortyCharactersClient
import com.oscarbarrera.apirikymorty.model.RikYMortyCharactersResult
import com.oscarbarrera.apirikymorty.ui.theme.ApiRikYMortyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

// Composable function to display the list of characters
@Composable
fun CharacterListScreen() {
    var characters by remember { mutableStateOf<List<Characters>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Fetch data from the API
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            RikYMortyCharactersClient.service.listCharacters(1)
                .enqueue(object : Callback<RikYMortyCharactersResult> {
                    override fun onResponse(
                        call: Call<RikYMortyCharactersResult>,
                        response: Response<RikYMortyCharactersResult>
                    ) {
                        if (response.isSuccessful) {
                            characters = response.body()?.results ?: emptyList()
                        }
                        isLoading = false
                    }

                    override fun onFailure(call: Call<RikYMortyCharactersResult>, t: Throwable) {
                        t.printStackTrace()
                        isLoading = false
                    }
                })
        }
    }

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

// Composable function for an individual character item
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
                Text(text = character.species, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


// Preview (Dummy Data)
@Preview(showBackground = true)
@Composable
fun CharacterItemPreview() {
    ApiRikYMortyTheme {
        CharacterItem(
            character = Characters(
                id = 1,
                name = "Rick Sanchez",
                species = "Human",
                gender = "Male",
                status = "Alive",
                type = "",
                created = "",
                episode = emptyList(),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                location = Location("Earth", ""),
                origin = Origin("Earth", ""),
                url = ""
            )
        )
    }
}
