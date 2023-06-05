package it.dario.malaapplicazione.data.model

import it.dario.malaapplicazione.data.Constants.NO_DISPONIBILITA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * rappresenta un animatore e le sue disponibilità date
 *
 * @param _nome nome dell'animatore
 * @param _cognome cognome dell'animatore
 * @param _domicilio indirizzo di domicilio dell'animatore
 * @param _auto disponibilità auto
 * @param _bambini disponibilità per attività bambini
 * @param _adulti disponibilità per attività adulti
 * @param _note note
 */
data class Animatore(
    val index: Int,
    val nome: String,
    val cognome: String,
    private var _domicilio: String = "",
    private var _auto: Boolean = false,
    private var _bambini: Boolean = false,
    private var _adulti: Boolean = false,
    private var _note: String = "",
    var dataAggiornamento: LocalDateTime = LocalDateTime.now().minusDays(1), //appena un animatore viene creato, lo considero "vecchio" in modo da aggiornalo quando serve
) {

    //duplicate per mantenere il set priivato
    val domicilio: String get() = _domicilio
    val note: String get() = _note
    val auto: Boolean get() = _auto
    val adulti: Boolean get() = _adulti
    val bambini: Boolean get() = _bambini

    private val domicilioFlow = MutableStateFlow(_domicilio)
    private val autoFlow = MutableStateFlow(_auto)
    private val bambiniFlow = MutableStateFlow(_bambini)
    private val adultiFlow = MutableStateFlow(_adulti)
    private val noteFlow = MutableStateFlow(_note)
    /**
     * etichetta che verrà usata per mostrare il nome nello spinner
     */
    val label: String get() = "$cognome $nome"

    /**
     * la disponibilità data dall'animatore per il giorno indicato come chiave
     */
    private val disponibilita: MutableMap<LocalDate, MutableStateFlow<String>> = mutableMapOf()

    fun setDisponibilita(date: LocalDate, content: String) {
        //disponibilita[date] = MutableStateFlow(content)
        if (disponibilita[date] == null) {
            disponibilita[date] = MutableStateFlow(content)
        } else {
            updateDisponibilita(date, content)
        }
    }

    fun updateDisponibilita(date: LocalDate, content: String) {
        disponibilita[date]!!.value = content
    }

    /**
     * restituisce la disponibilità dell'animatore per una determinata data
     *
     * @param date la data da prendere in considerazione
     * @return la disponibilità data dall'animatore, o [NO_DISPONIBILITA] in assenza
     */
    fun getDisponibilita(date: LocalDate) : String {
        return disponibilita[date]?.value ?: NO_DISPONIBILITA
    }

    fun getDisponibilitaAsFlow(date: LocalDate) : StateFlow<String> {
        return disponibilita[date]?.asStateFlow() ?: run {
            disponibilita[date] = MutableStateFlow("")
            return disponibilita[date]!!.asStateFlow() }
    }

    fun getDomicilioAsFlow(): StateFlow<String> {
        return domicilioFlow.asStateFlow()
    }

    fun updateDomicilio (value: String) {
        _domicilio = value
        domicilioFlow.value = value
    }

    fun getAutoAsFlow(): StateFlow<Boolean> {
        return autoFlow.asStateFlow()
    }

    fun updateAuto(value: Boolean) {
        _auto = value
        autoFlow.value = value
    }

    fun getBambiniAsFlow (): StateFlow<Boolean> {
        return bambiniFlow.asStateFlow()
    }

    fun updateBambini (value: Boolean) {
        _bambini = value
        bambiniFlow.value = value
    }

    fun getAdultiAsFlow (): StateFlow<Boolean> {
        return adultiFlow.asStateFlow()
    }

    fun updateAdulti (value: Boolean) {
        _adulti = value
        adultiFlow.value = value
    }

    fun getNoteAsFlow(): StateFlow<String> {
        return noteFlow.asStateFlow()
    }

    fun updateNote (value: String) {
        _note = value
        noteFlow.value = value
    }

}
