package com.oscarbarrera.apirikymorty.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.oscarbarrera.apirikymorty.model.RikYMortyCharactersClient
import com.oscarbarrera.apirikymorty.model.Characters

class RickYMortyViewModel : ViewModel() {
    // Lista de personajes
    private val _characterList = MutableStateFlow<List<Characters>>(emptyList())
    val characterList: StateFlow<List<Characters>> = _characterList

    init {
        fetchCharacterList()
    }

    // Carga la lista de personajes desde la API
    private fun fetchCharacterList() {
        viewModelScope.launch {
            try {
                // Llama a la API para obtener la lista de personajes (página 1)
                val response = RikYMortyCharactersClient.service.listCharacters(1)
                _characterList.value = response.results ?: emptyList() // Asigna los resultados directamente
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}