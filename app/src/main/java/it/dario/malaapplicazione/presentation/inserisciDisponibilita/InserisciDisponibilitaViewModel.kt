package it.dario.malaapplicazione.presentation.inserisciDisponibilita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    val mesi = repository.getFogli()
    var listAnimatori = listOf<Animatore>()


    fun updateFoglioSelezionato(newValue: String) {
        fetchAnimatoriInFoglio(newValue)
        _foglioSelezionato.value = newValue
        _animatoreSelezionato.value = null
    }

    fun updateAnimatoreSelezionato(newValue: String) {
        _loadingAnimatore.value = true
        viewModelScope.launch(IO) {
            repository.refreshAnimatore(foglioSelezionato.value!!, newValue)
            _loadingAnimatore.value = false
        }
        _animatoreSelezionato.value = newValue
    }


    private fun fetchAnimatoriInFoglio(foglio: String) = CoroutineScope(IO).launch {
        _loadingFoglio.value = true
        listAnimatori = repository.getAnimatori(foglio)
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

    fun getBambiniAsFlow(foglio: String, animatore: String) =
        repository.getBambiniAsFlow(foglio, animatore)

    fun updateBambini(foglio: String, animatore: String, value: Boolean) =
        CoroutineScope(IO).launch { repository.updateBambini(foglio, animatore, value) }

    fun getAdultiAsFlow(foglio: String, animatore: String) =
        repository.getAdultiAsFlow(foglio, animatore)

    fun updateAdulti(foglio: String, animatore: String, value: Boolean) =
        CoroutineScope(IO).launch { repository.updateAdulti(foglio, animatore, value) }
}

@Suppress("UNCHECKED_CAST")
class InserisciDisponibilitaViewModelFactory(
    private val repository: DisponibilitaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InserisciDisponibilitaViewModel(repository = repository) as T
    }
}
