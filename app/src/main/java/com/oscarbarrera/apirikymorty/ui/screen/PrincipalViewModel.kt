package com.oscarbarrera.apirikymorty.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.oscarbarrera.apirikymorty.data.FirestoreManager
import com.oscarbarrera.apirikymorty.model.Characters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrincipalViewModel(val firestoreManager: FirestoreManager) : ViewModel() {

    val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _personaje = MutableStateFlow<Characters?>(null)
    val personaje: StateFlow<Characters?> = _personaje

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            firestoreManager.getCharacter().collect { personajes ->
                _uiState.update { uiState ->
                    uiState.copy(
                        personajes = personajes,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun addCharacter(personaje: Characters) {
        viewModelScope.launch {
            firestoreManager.addCharacter(personaje)
        }
    }

    fun deleteCharacterById(characterId: String) {
        if (characterId.isEmpty()) return
        viewModelScope.launch {
            firestoreManager.deleteCharacterById(characterId)
        }
    }

    fun updateCharacter(characterNew: Characters) {
        viewModelScope.launch {
            firestoreManager.updateCharacter(characterNew)
        }
    }

    fun getCharacterById(characterId: String) {
        viewModelScope.launch {
            _personaje.value = firestoreManager.getCharacterById(characterId)
        }
    }

    fun onAddCharacterSelected() {
        _uiState.update { it.copy(showAddCharacterDialog = true) }
    }

    fun dismisShowAddCharacterDialog() {
        _uiState.update { it.copy(showAddCharacterDialog = false) }
    }

    fun onLogoutSelected() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun dismisShowLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }
}

data class UiState(
    val personajes: List<Characters> = emptyList(),
    val isLoading: Boolean = false,
    val showAddCharacterDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)

class PrincipalViewModelFactory(private val firestoreManager: FirestoreManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PrincipalViewModel(firestoreManager) as T
    }
}