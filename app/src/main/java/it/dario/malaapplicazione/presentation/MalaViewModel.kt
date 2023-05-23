package it.dario.malaapplicazione.presentation.visualizzaDisponibilita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import kotlinx.coroutines.flow.*

class MalaViewModel (val repository: DisponibilitaRepository) : ViewModel() {


}

@Suppress("UNCHECKED_CAST")
class MalaViewModelFactory(
    private val repository: DisponibilitaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MalaViewModel(repository = repository) as T
    }
}
