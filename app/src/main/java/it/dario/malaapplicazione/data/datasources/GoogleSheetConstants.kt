package it.dario.malaapplicazione.data.datasources

import java.time.Month

object GoogleSheetConstants {

    const val INDICE_RIGA_PRIMO_ANIMATORE = 4 //evitando Mario Rossi
    const val INDICE_RIGA_GIORNI = 2

    const val INDICE_COLONNA_NOME = 'A'
    const val INDICE_COLONNA_COGNOME = 'B'
    const val INDICE_COLONNA_PRIMO_GIORNO = 'G'
    const val INDICE_COLONNA_PRIMO_GIORNO_INT = 7


    const val INDICE_COLONNA_RESIDENZA = 'C'
    const val INDICE_COLONNA_AUTO = 'D'
    const val INDICE_COLONNA_ADULTI = 'E'
    const val INDICE_COLONNA_BAMBINI = 'F'

    const val COLONNE_NOME_COGNOME_ANIMATORI =
        "$INDICE_COLONNA_NOME$INDICE_RIGA_PRIMO_ANIMATORE:$INDICE_COLONNA_COGNOME"

    const val RIGA_GIORNI = "$INDICE_COLONNA_PRIMO_GIORNO$INDICE_RIGA_GIORNI:$INDICE_RIGA_GIORNI"

    const val FILE_NAME_SEPARATOR = " '"

    const val FILENAME_PATTERN = """^.* '\d{2}$"""
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