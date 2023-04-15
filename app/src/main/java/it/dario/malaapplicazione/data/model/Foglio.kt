package it.dario.malaapplicazione.data.model

import java.time.LocalDate

/**
 * Rappresenta un foglio dell'excel
 *
 * @param meseString il mese in formato stringa, come da nome del foglio
 * @param annoString l'anno in formato stringa, come da nome del foglio (le ultime due cifre)
 * @param meseInt il mese in formato numerico
 * @param annoInt l'anno in formato numerico
 * @param primoGiorno il primo giorno presente sul foglio
 * @param ultimoGiorno l'ultimo giorno compilabile sul file excel
 */
data class Foglio(
    val meseString: String,
    val annoString: String,
    val meseInt: Int,
    val annoInt: Int,
    val primoGiorno: LocalDate,
    val ultimoGiorno: LocalDate
) {

    /**
     * etichetta che verrà usata per mostrare il nome nello spinner
     */
    val label: String get() = "$meseString'$annoString"

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


}