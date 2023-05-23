package it.dario.malaapplicazione.presentation.visualizzaDisponibilita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.InserisciUiState
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class InserisciDisponibilitaViewModel(val repository: DisponibilitaRepository) : ViewModel() {


    private val _uiState: MutableStateFlow<InserisciUiState> =
        MutableStateFlow(InserisciUiState(null, null))
    val uiState: StateFlow<InserisciUiState> get() = _uiState.asStateFlow()

    var foglioSelezionato: String? = null
    var animatoreSelezionato: String? = null

    val mesi = repository.getFogli()

    fun updateFoglioSelezionato(newValue: String) {
        if (foglioSelezionato != newValue) {
            foglioSelezionato = newValue
            animatoreSelezionato = null
            _uiState.value = (InserisciUiState(foglioSelezionato, animatoreSelezionato))
        }
    }

    fun updateAnimatoreSelezionato(newValue: String) {
        if (animatoreSelezionato != newValue) {
            animatoreSelezionato = newValue
            _uiState.value = (InserisciUiState(foglioSelezionato, animatoreSelezionato))
        }
    }

    fun getAnimatoriInFoglio(foglio: String): List<Animatore> {
        return repository.getAnimatori(foglio)
    }

    fun getFoglio(foglio: String): Foglio {
        return repository.getFoglio(foglio)
    }

    fun getAnimatore(foglio: String, animatore: String): Animatore {
        return repository.getAnimatore(foglio, animatore)
    }

    fun getDisponibilitaAsFlow(foglio: String, animatore: String, day: LocalDate) =
        repository.getDisponibilitaAsFlow(foglio, animatore, day)

    fun updateAnimatoreDisponibilita(
        foglio: String,
        animatore: String,
        day: LocalDate,
        newValue: String
    ) {
        repository.setDisponibilita(foglio, animatore, day, newValue)
    }

    fun updateDomicilio(foglio: String, animatore: String, value: String) = repository.updateDomicilio(foglio, animatore, value)
    fun updateNote(foglio: String, animatore: String, value: String) = repository.updateNote(foglio, animatore, value)

    fun updateAuto(foglio: String, animatore: String, value: Boolean) = repository.updateAuto(foglio, animatore, value)
    fun updateBambini(foglio: String, animatore: String, value: Boolean) = repository.updateBambini(foglio, animatore, value)
    fun updateAdulti(foglio: String, animatore: String, value: Boolean) = repository.updateAdulti(foglio, animatore, value)
}

@Suppress("UNCHECKED_CAST")
class InserisciDisponibilitaViewModelFactory(
    private val repository: DisponibilitaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InserisciDisponibilitaViewModel(repository = repository) as T
    }
}
