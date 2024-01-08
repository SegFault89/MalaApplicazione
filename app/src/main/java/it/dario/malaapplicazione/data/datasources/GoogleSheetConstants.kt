package it.dario.malaapplicazione.data.datasources

import java.time.Month

object GoogleSheetConstants {

    const val INDICE_RIGA_PRIMO_ANIMATORE = 4 //evitando Mario Rossi
    const val INDICE_RIGA_GIORNI = 2
    const val OFFSET_PRIMO_GIORNO_SENZA_NOMI = 2 //quante celle, senza prendere in considerazione quelle del nome, ci sono prima del primo giorno

    const val INDICE_COLONNA_NOME = 'A'
    const val INDICE_COLONNA_PRIMO_GIORNO = 'D'
    const val INDICE_COLONNA_RESIDENZA = 'B'
    const val INDICE_COLONNA_AUTO = 'C'

    const val INDICE_COLONNA_NOME_INT = 1
    const val INDICE_COLONNA_RESIDENZA_INT = 2
    const val INDICE_COLONNA_AUTO_INT = 3
    const val INDICE_COLONNA_PRIMO_GIORNO_INT = 4

    fun getRigheAnimatoriCompleti (indiceNote: Int): String =
        "R${INDICE_RIGA_PRIMO_ANIMATORE}C$INDICE_COLONNA_NOME_INT:C$indiceNote"

    const val RIGA_GIORNI = "$INDICE_COLONNA_PRIMO_GIORNO$INDICE_RIGA_GIORNI:$INDICE_RIGA_GIORNI"
    const val RANGE_NOMI = "$INDICE_COLONNA_NOME$INDICE_RIGA_PRIMO_ANIMATORE:$INDICE_COLONNA_NOME"


    const val FILENAME_PATTERN = """^.*\d{2}$""" // vecchio pattern con ' """^.*'\d{2}$"""
    val FILENAME_REGEX = Regex(FILENAME_PATTERN)

    const val BASE_YEAR = 2000

    const val RAW = "RAW"

    const val GENNAIO = "gennaio"
    const val FEBBRAIO = "febbraio"
    const val MARZO = "marzo"
    const val APRILE = "aprile"
    const val MAGGIO = "maggio"
    const val GIUGNO = "giugno"
    const val LUGLIO = "luglio"
    const val AGOSTO = "agosto"
    const val SETTEMBRE = "settembre"
    const val OTTOBRE = "ottobre"
    const val NOVEMBRE = "novembre"
    const val DICEMBRE = "dicembre"

    val mapMonth = mapOf(
        GENNAIO to Month.JANUARY.value,
        FEBBRAIO to Month.FEBRUARY.value,
        MARZO to Month.MARCH.value,
        APRILE to Month.APRIL.value,
        MAGGIO to Month.MAY.value,
        GIUGNO to Month.JUNE.value,
        LUGLIO to Month.JULY.value,
        AGOSTO to Month.AUGUST.value,
        SETTEMBRE to Month.SEPTEMBER.value,
        OTTOBRE to Month.OCTOBER.value,
        NOVEMBRE to Month.NOVEMBER.value,
        DICEMBRE to Month.DECEMBER.value
    )


}