package it.dario.malaapplicazione.presentation.inserisciDisponibilita

import it.dario.malaapplicazione.data.model.Animatore

data class InserisciUiState (
    val foglioSelezionato: String? = null,
    val animatoreSelezionato: Animatore? = null
)