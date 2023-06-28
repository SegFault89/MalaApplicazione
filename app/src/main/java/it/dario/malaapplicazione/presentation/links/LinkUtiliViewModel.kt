package it.dario.malaapplicazione.presentation.links

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.dario.malaapplicazione.data.model.LinkSection
import it.dario.malaapplicazione.domain.repositories.FirebaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class LinkUtiliViewModel(private val repository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {

    init {
        fetchLinks()
    }

    private val _links: MutableLiveData<List<LinkSection>?> = MutableLiveData()
    val links get() = _links

    @ExperimentalCoroutinesApi
    fun fetchLinks() {
        viewModelScope.launch {
            repository.fetchLinks().collect {
                when {
                    it.isSuccess -> {
                        val list = it.getOrNull()
                        _links.value = list
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }
}


