package it.dario.malaapplicazione.data.datasources

import android.content.Context
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

sealed interface IDisponibilitaDataSource {

    /**
     * indica se il datasource è pronto per esere utilizzato
     */
    val isReady: StateFlow<Boolean>

    /**
     * restituisce una lista dei fogli presenti sul file
     */
    fun getFogli() : List<String>

    /**
     * restituisce una lista degli animatori presenti nel foglio indicato
     */
    suspend fun getAnimatori(foglio: String): List<Animatore>
    fun getFoglio(name: String): Foglio
    fun getDisponibilitaAsFlow(foglio: String, animatore: String, date: LocalDate): StateFlow<String>
    fun getAnimatore(foglio: String, animatore: String): Animatore

    fun getDomicilioAsFlow(foglio: String, animatore: String): StateFlow<String>
    fun getAutoAsFlow(foglio: String, animatore: String): StateFlow<Boolean>
    fun getBambiniAsFlow (foglio: String, animatore: String): StateFlow<Boolean>
    fun getAdultiAsFlow (foglio: String, animatore: String): StateFlow<Boolean>
    fun getNoteAsFlow(foglio: String, animatore: String): StateFlow<String>

    suspend fun updateDisponibilita(foglio: String, animatore: String, date: LocalDate, content: String)
    suspend fun updateDomicilio (foglio: String, animatore: String, value: String)
    suspend fun updateAuto(foglio: String, animatore: String, value: Boolean)
    suspend fun updateBambini (foglio: String, animatore: String, value: Boolean)
    suspend fun updateAdulti (foglio: String, animatore: String, value: Boolean)
    suspend fun updateNote (foglio: String, animatore: String, value: String)

    suspend fun refreshAnimatore(foglio: String, animatore: String)

    suspend fun setup(context: Context)
}