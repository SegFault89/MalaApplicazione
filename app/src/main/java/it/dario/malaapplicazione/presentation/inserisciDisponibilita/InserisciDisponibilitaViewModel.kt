package it.dario.malaapplicazione.presentation.visualizzaDisponibilita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.dario.malaapplicazione.data.model.Animatore
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

    fun getPrimoGiorno(foglio: String): LocalDate {
        return repository.getFoglio(foglio).primoGiorno
    }

    fun getUltimoGiorno(foglio: String): LocalDate {
        return repository.getFoglio(foglio).ultimoGiorno
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

    fun getDomicilioAsFlow(foglio: String, animatore: String) =
        repository.getDomicilioAsFlow(foglio, animatore)

    fun updateDomicilio(foglio: String, animatore: String, value: String) = repository.updateDomicilio(foglio, animatore, value)

    fun getNoteAsFlow(foglio: String, animatore: String) =
        repository.getNoteAsFlow(foglio, animatore)

    fun updateNote(foglio: String, animatore: String, value: String) = repository.updateNote(foglio, animatore, value)

    fun getAutoAsFlow(foglio: String, animatore: String) =
        repository.getAutoAsFlow(foglio, animatore)

    fun updateAuto(foglio: String, animatore: String, value: Boolean) = repository.updateAuto(foglio, animatore, value)

    fun getBambiniAsFlow(foglio: String, animatore: String) =
        repository.getBambiniAsFlow(foglio, animatore)

    fun updateBambini(foglio: String, animatore: String, value: Boolean) = repository.updateBambini(foglio, animatore, value)

    fun getAdultiAsFlow(foglio: String, animatore: String) =
        repository.getAdultiAsFlow(foglio, animatore)

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
