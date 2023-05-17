package it.dario.malaapplicazione.data.datasources

import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio

sealed interface IDisponibilitaDataSource {
    fun getMesi() : List<String>
    fun getAnimatori(mese: String): List<Animatore>
    fun getAnimatore(animatore: String): Animatore?
    fun getFoglio(name: String): Foglio
}