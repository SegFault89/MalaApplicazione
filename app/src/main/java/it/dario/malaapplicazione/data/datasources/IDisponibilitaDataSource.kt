package it.dario.malaapplicazione.data.datasources

import it.dario.malaapplicazione.data.model.Animatore

sealed interface IDisponibilitaDataSource {
    fun getMesi() : List<String>
    fun getAnimatori(mese: String): List<Animatore>
    fun getAnimatore(animatore: String): Animatore?
}