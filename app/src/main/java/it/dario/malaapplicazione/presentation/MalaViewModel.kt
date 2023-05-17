package it.dario.malaapplicazione.presentation.visualizzaDisponibilita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.dario.malaapplicazione.data.model.Foglio
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import kotlinx.coroutines.flow.*

class MalaViewModel (val repository: DisponibilitaRepository) : ViewModel() {

    val mesi = repository.getMesi()

    private var meseSelezionatoStr: MutableStateFlow<String> = MutableStateFlow("")
    private var animatoreSelezionatoStr: MutableStateFlow<String> = MutableStateFlow("")

    fun selezionaMese(mese: String) {
        meseSelezionatoStr.value = mese
    }
    fun selezionaAnimatore(mese: String) {
        animatoreSelezionatoStr.value = mese
    }

    val foglio: Foglio get() = repository.getFoglio(meseSelezionatoStr.value)

    val animatori =
        meseSelezionatoStr.debounce(200).distinctUntilChanged().map { repository.getAnimatori(it) }

    val disponibilitaAnimatore =
        animatoreSelezionatoStr.debounce(200).distinctUntilChanged().map { repository.getAnimatore(it) }
}

@Suppress("UNCHECKED_CAST")
class MalaViewModelFactory(
    private val repository: DisponibilitaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MalaViewModel(repository = repository) as T
    }
}
