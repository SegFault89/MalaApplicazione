package it.dario.malaapplicazione.presentation.visualizzadisponibilita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.dario.malaapplicazione.data.model.DisponibilitaGiornaliere
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class VisualizzaDisponibilitaViewModel(val repository: DisponibilitaRepository) : ViewModel() {

    private val _loadingFoglio: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var loadingFoglio: StateFlow<Boolean> = _loadingFoglio.asStateFlow()

    private val _giornoSelezionato: MutableStateFlow<LocalDate?> = MutableStateFlow(null)
    var giornoSelezionato: StateFlow<LocalDate?> = _giornoSelezionato.asStateFlow()

    private val _foglioSelezionato: MutableStateFlow<String?> = MutableStateFlow(null)
    var foglioSelezionato: StateFlow<String?> = _foglioSelezionato.asStateFlow()

    val mesi = repository.getFogli()

    fun updateFoglioSelezionato(newValue: String) {
        _giornoSelezionato.value = null
        _loadingFoglio.value = true
        fetchFoglio(newValue)
        _foglioSelezionato.value = newValue
    }

    fun updateSelectedDay(day: LocalDate) {
        _giornoSelezionato.value = day
    }

    fun getAnimatoriDisponibili(foglio: String, giorno: LocalDate) =
        repository.getAnimatoriDisponibili(foglio, giorno)

    fun refreshSheet(onComplete: () -> Unit, onError: () -> Unit) = CoroutineScope(IO).launch {
        foglioSelezionato.value?.let {
            _loadingFoglio.value = true
            repository.fetchAnimatori(it, true, true)
            _loadingFoglio.value = false
            onComplete()
        } ?: onError()
    }

    private fun fetchFoglio(foglio: String) = CoroutineScope(IO).launch {
        repository.fetchAnimatori(foglio, true)
        _loadingFoglio.value = false
    }

    fun getPrimoGiorno(foglio: String): LocalDate {
        return repository.getFoglio(foglio).primoGiorno
    }

    fun getUltimoGiorno(foglio: String): LocalDate {
        return repository.getFoglio(foglio).ultimoGiorno
    }

    fun getDisponibilitaGiornaliere(foglio: String, day: LocalDate): DisponibilitaGiornaliere {
        return repository.getDisponibilitaGiornaliere(foglio, day)
    }
}


@Suppress("UNCHECKED_CAST")
class VisualizzaDisponibilitaViewModelFactory(
    private val repository: DisponibilitaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VisualizzaDisponibilitaViewModel(repository = repository) as T
    }
}
