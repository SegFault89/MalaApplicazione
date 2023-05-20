package it.dario.malaapplicazione.presentation.visualizzaDisponibilita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.InserisciUiState
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class InserisciDisponibilitaViewModel (val repository: DisponibilitaRepository) : ViewModel() {


    private val _uiState : MutableStateFlow<InserisciUiState> = MutableStateFlow(InserisciUiState(null, null))
    val uiState: StateFlow<InserisciUiState> get () = _uiState.asStateFlow()

    var foglioSelezionato: String? = null
    var animatoreSelezionato: Animatore? = null

    val mesi = repository.getMesi()

    fun updateFoglioSelezionato (newValue: String) {
        if (foglioSelezionato != newValue){
            foglioSelezionato = newValue
            animatoreSelezionato = null
            _uiState.value =(InserisciUiState(foglioSelezionato, animatoreSelezionato))
        }
    }

    fun updateAnimatoreSelezionato (newValue: Animatore) {
        if (animatoreSelezionato != newValue){
            animatoreSelezionato = newValue
            _uiState.value =(InserisciUiState(foglioSelezionato, animatoreSelezionato))
        }
    }

    fun getAnimatoriInFoglio(foglio: String): List<Animatore> {
        return repository.getAnimatori(foglio)
    }

    fun getFoglio(foglio: String): Foglio {
        return repository.getFoglio(foglio)
    }

    fun updateAnimatoreDisponibilita(animatore: Animatore, foglio: Foglio, day: LocalDate, newValue: String) {
        //TODO passare da repository per aggiornamento online
        animatore.updateDisponibilita(day, newValue)
    }

    /*
        private var _animatoreSelezionato: MutableStateFlow<Animatore?> = MutableStateFlow(null)


        fun updateUiState(newEvent: InserisciEvent) {
        val newState =
            when (newEvent) {
                is InserisciEvent.eventSheetChanged -> InserisciUiState(newEvent.newSheet, null)
                is InserisciEvent.eventAnimatorChanged -> InserisciUiState(foglioSelezionato.foglio, null)animatoreSelezionato = newState.newAnimatore
            }
            _uiState.value = newState
        }


        fun selezionaAnimatore(mese: String) {
            animatoreSelezionatoStr.value = mese
        }


        val foglio: Foglio get() = repository.getFoglio(meseSelezionatoStr.value)

        val animatori = foglioSelezionato?.let { repository.getAnimatori(it) }

        val disponibilitaAnimatore =
            animatoreSelezionatoStr.debounce(100).distinctUntilChanged().map { repository.getAnimatore(it) }

        val getAnimatore = repository.getAnimatore(animatoreSelezionatoStr.value)*/
}

@Suppress("UNCHECKED_CAST")
class InserisciDisponibilitaViewModelFactory(
    private val repository: DisponibilitaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InserisciDisponibilitaViewModel(repository = repository) as T
    }
}
