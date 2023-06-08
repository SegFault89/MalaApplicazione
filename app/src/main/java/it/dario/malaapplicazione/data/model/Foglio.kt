package it.dario.malaapplicazione.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Rappresenta un foglio dell'excel
 *
 * @param meseString il mese in formato stringa, come da nome del foglio
 * @param annoString l'anno in formato stringa, come da nome del foglio (le ultime due cifre)
 * @param primoGiorno il primo giorno presente sul foglio
 * @param ultimoGiorno l'ultimo giorno compilabile sul file excel
 */
data class Foglio(
    val label: String,
    val primoGiorno: LocalDate,
    val ultimoGiorno: LocalDate,
    val dataAggiornamento: LocalDateTime = LocalDateTime.now().minusDays(1)
) {

    /**
     * gli animatori indicati nel foglio presenze
     * viene usata una mappa per facilità di ricerca
     */
    private val _animatori: MutableMap<String, Animatore> = mutableMapOf()

    val animatori : Map<String, Animatore> get() = _animatori
    /**
     * aggiunge un animatore al mese
     */
    fun addAnimatore(key: String, animatore: Animatore) {
        _animatori.put(key, animatore)
    }

    fun getAnimatoriAsList() = animatori.values.toList()


    val dayNum = ChronoUnit.DAYS.between(primoGiorno, ultimoGiorno) +1 // +1 per includere l'ultimo


}