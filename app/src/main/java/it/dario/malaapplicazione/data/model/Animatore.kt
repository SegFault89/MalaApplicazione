package it.dario.malaapplicazione.data.model

import it.dario.malaapplicazione.data.Constants.NO_DISPONIBILITA
import java.time.LocalDate

/**
 * rappresenta un animatore e le sue disponibilità date
 *
 * @param nome nome dell'animatore
 * @param cognome cognome dell'animatore
 * @param domicilio indirizzo di domicilio dell'animatore
 * @param auto disponibilità auto
 * @param bambini disponibilità per attività bambini
 * @param adulti disponibilità per attività adulti
 * @param note note
 */
data class Animatore(
    val nome: String,
    val cognome: String,
    var domicilio: String,
    var auto: Boolean,
    var bambini: Boolean,
    var adulti: Boolean,
    var note: String,
) {

    /**
     * etichetta che verrà usata per mostrare il nome nello spinner
     */
    val label: String get() = "$cognome $nome"

    /**
     * la disponibilità data dall'animatore per il giorno indicato come chiave
     */
    private val disponibilita: MutableMap<LocalDate, String> = mutableMapOf()

    fun setDisponibilita(date: LocalDate, content: String) {
        disponibilita[date] = content
    }

    /**
     * restituisce la disponibilità dell'animatore per una determinata data
     *
     * @param date la data da prendere in considerazione
     * @return la disponibilità data dall'animatore, o [NO_DISPONIBILITA] in assenza
     */
    fun getDisponibilita(date: LocalDate) : String {
        return disponibilita[date] ?: NO_DISPONIBILITA
    }
}