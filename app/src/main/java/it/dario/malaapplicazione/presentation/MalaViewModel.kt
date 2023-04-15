package it.dario.malaapplicazione.presentation.visualizzaDisponibilita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import kotlinx.coroutines.flow.*

class MalaViewModel (val repository: DisponibilitaRepository) : ViewModel() {

    val mesi = repository.getMesi()

    private var meseSelezionato: MutableStateFlow<String> = MutableStateFlow("")
    private var animatoreSelezionato: MutableStateFlow<String> = MutableStateFlow("")

    fun selezionaMese(mese: String) {
        meseSelezionato.value = mese
    }
    fun selezionaAnimatore(mese: String) {
        meseSelezionato.value = mese
    }

    val animatori =
        meseSelezionato.debounce(200).distinctUntilChanged().map { repository.getAnimatori(it) }

    val disponibilitàAnimatore =
        animatoreSelezionato.debounce(200).distinctUntilChanged().map { repository.getAnimatori(it) }
}

@Suppress("UNCHECKED_CAST")
class MalaViewModelFactory(
    private val repository: DisponibilitaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MalaViewModel(repository = repository) as T
    }
}
