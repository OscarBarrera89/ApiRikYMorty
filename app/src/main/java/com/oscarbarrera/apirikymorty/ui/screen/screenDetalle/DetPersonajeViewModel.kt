package com.oscarbarrera.apirikymorty.ui.screen.screenDetalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.oscarbarrera.apirikymorty.data.FirestoreManager
import com.oscarbarrera.apirikymorty.model.PlanetaProcedencia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlanetaViewModel(val firestoreManager: FirestoreManager, val idPersonaje: String) : ViewModel() {

    val _uiState = MutableStateFlow(UiStateDetalle())
    val uiState: StateFlow<UiStateDetalle> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            firestoreManager.getPlanetasByPersonajeId(idPersonaje).collect { planetas ->
                _uiState.update { uiState ->
                    uiState.copy(
                        planetas = planetas,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun addPlaneta(planeta: PlanetaProcedencia) {
        viewModelScope.launch {
            firestoreManager.addPlaneta(planeta)
        }
    }

    fun updatePlaneta(planetaNew: PlanetaProcedencia) {
        viewModelScope.launch {
            firestoreManager.updatePlaneta(planetaNew)
        }
    }

    fun deletePlanetaById(planetaId: String) {
        if (planetaId.isEmpty()) return
        viewModelScope.launch {
            firestoreManager.deletePlanetaById(planetaId)
        }
    }

    fun onAddPlanetaSelected() {
        _uiState.update { it.copy(showAddPlanetaDialog = true) }
    }

    fun dismisShowAddPlanetaDialog() {
        _uiState.update { it.copy(showAddPlanetaDialog = false) }
    }
}

data class UiStateDetalle(
    val planetas: List<PlanetaProcedencia> = emptyList(),
    val isLoading: Boolean = false,
    val showAddPlanetaDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)

class PlanetaViewModelFactory(private val firestoreManager: FirestoreManager, private val idPersonaje: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlanetaViewModel(firestoreManager, idPersonaje) as T
    }
}