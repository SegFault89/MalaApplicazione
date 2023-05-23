package it.dario.malaapplicazione.data.datasources

import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

sealed interface IDisponibilitaDataSource {
    fun getFogli() : List<String>
    fun getAnimatori(foglio: String): List<Animatore>
    fun getFoglio(name: String): Foglio
    fun getDisponibilitaAsFlow(foglio: String, animatore: String, date: LocalDate): StateFlow<String>
    fun setDisponibilita(foglio: String, animatore: String, date: LocalDate, content: String)
    fun getAnimatore(foglio: String, animatore: String): Animatore

    fun getDomicilioAsFlow(foglio: String, animatore: String): StateFlow<String>
    fun getAutoAsFlow(foglio: String, animatore: String): StateFlow<Boolean>
    fun getBambiniAsFlow (foglio: String, animatore: String): StateFlow<Boolean>
    fun getAdultiAsFlow (foglio: String, animatore: String): StateFlow<Boolean>
    fun getNoteAsFlow(foglio: String, animatore: String): StateFlow<String>

    fun updateDomicilio (foglio: String, animatore: String, value: String)
    fun updateAuto(foglio: String, animatore: String, value: Boolean)
    fun updateBambini (foglio: String, animatore: String, value: Boolean)
    fun updateAdulti (foglio: String, animatore: String, value: Boolean)
    fun updateNote (foglio: String, animatore: String, value: String)
}