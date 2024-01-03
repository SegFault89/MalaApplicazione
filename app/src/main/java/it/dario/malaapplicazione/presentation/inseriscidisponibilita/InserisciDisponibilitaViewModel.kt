package it.dario.malaapplicazione.presentation.inseriscidisponibilita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
class InserisciDisponibilitaViewModel(val repository: DisponibilitaRepository) : ViewModel() {


    private val _loadingFoglio: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var loadingFoglio: StateFlow<Boolean> = _loadingFoglio.asStateFlow()


    private val _loadingAnimatore: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var loadingAnimatore: StateFlow<Boolean> = _loadingAnimatore.asStateFlow()


    private val _foglioSelezionato: MutableStateFlow<String?> = MutableStateFlow(null)
    var foglioSelezionato: StateFlow<String?> = _foglioSelezionato.asStateFlow()

    private val _animatoreSelezionato: MutableStateFlow<String?> = MutableStateFlow(null)
    var animatoreSelezionato: StateFlow<String?> = _animatoreSelezionato.asStateFlow()

    fun getFogli() = repository.getFogli()
    var listAnimatori = listOf<Animatore>()


    fun updateFoglioSelezionato(newValue: String?) = runBlocking {
        _foglioSelezionato.value = newValue
        newValue?.let { fetchAnimatoriInFoglio(it) }

        //se nel nuovo foglio selezionato ho già l'animatore selezionato, lo refressho
        if ((animatoreSelezionato.value != null && listAnimatori.any { it.label == animatoreSelezionato.value })) {
            updateAnimatoreSelezionato(_animatoreSelezionato.value)
        } else {
            _animatoreSelezionato.value = null
        }
    }

    fun updateAnimatoreSelezionato(newValue: String?) {
        _loadingAnimatore.value = true
        viewModelScope.launch(IO) {
            newValue?.let { repository.refreshAnimatore(foglioSelezionato.value!!, it) }
            _loadingAnimatore.value = false
        }
        _animatoreSelezionato.value = newValue
    }


    private suspend fun fetchAnimatoriInFoglio(foglio: String) {
        _loadingFoglio.value = true
        listAnimatori = repository.fetchAnimatori(foglio).sortedBy { it.label }
        _loadingFoglio.value = false
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
    ) = CoroutineScope(IO).launch { repository.setDisponibilita(foglio, animatore, day, newValue) }

    fun getDomicilioAsFlow(foglio: String, animatore: String) =
        repository.getDomicilioAsFlow(foglio, animatore)

    fun updateDomicilio(foglio: String, animatore: String, value: String) =
        CoroutineScope(IO).launch { repository.updateDomicilio(foglio, animatore, value) }

    fun getNoteAsFlow(foglio: String, animatore: String) =
        repository.getNoteAsFlow(foglio, animatore)

    fun updateNote(foglio: String, animatore: String, value: String) =
        CoroutineScope(IO).launch { repository.updateNote(foglio, animatore, value) }

    fun getAutoAsFlow(foglio: String, animatore: String) =
        repository.getAutoAsFlow(foglio, animatore)

    fun updateAuto(foglio: String, animatore: String, value: Boolean) =
        CoroutineScope(IO).launch { repository.updateAuto(foglio, animatore, value) }

}

@Suppress("UNCHECKED_CAST")
class InserisciDisponibilitaViewModelFactory(
    private val repository: DisponibilitaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InserisciDisponibilitaViewModel(repository = repository) as T
    }
}
