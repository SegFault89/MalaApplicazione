package it.dario.malaapplicazione.data.datasources

object GoogleSheetConstants {

    const val INDICE_RIGA_PRIMO_ANIMATORE = "4" //evitando Mario Rossi
    const val INDICE_RIGA_GIORNI = "2"

    const val INDICE_COLONNA_NOME = "A"
    const val INDICE_COLONNA_COGNOME = "B"
    const val INDICE_COLONNA_PRIMO_GIORNO = "G"

    const val COLONNE_NOME_COGNOME_ANIMATORI = "$INDICE_COLONNA_NOME$INDICE_RIGA_PRIMO_ANIMATORE:$INDICE_COLONNA_COGNOME"

    const val RIGA_GIORNI = "$INDICE_COLONNA_PRIMO_GIORNO$INDICE_RIGA_GIORNI:$INDICE_RIGA_GIORNI"
}