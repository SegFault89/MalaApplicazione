package it.dario.malaapplicazione.data.repositories

import it.dario.malaapplicazione.data.datasources.GoogleSheetDataSource
import it.dario.malaapplicazione.data.datasources.IDisponibilitaDataSource
import it.dario.malaapplicazione.data.enums.DisponibilitaEnum
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.DisponibilitaGiornaliere
import it.dario.malaapplicazione.data.model.Foglio
import java.time.LocalDate

class DisponibilitaRepository(val datasource: IDisponibilitaDataSource) {

    val isReady = datasource.isReady

    fun getFogli(): List<String> {
        return datasource.getFogli()
    }

    fun getFoglio(nome: String): Foglio {
        return datasource.getFoglio(nome)
    }

    suspend fun fetchAnimatori(foglio: String, complete: Boolean = false): List<Animatore> {
        return datasource.fetchAnimatoriInFoglio(foglio, complete)
    }

    fun getDisponibilitaAsFlow(foglio: String, animatore: String, date: LocalDate) =
        datasource.getDisponibilitaAsFlow(foglio = foglio, animatore = animatore, date = date)

    fun getAnimatore(foglio: String, animatore: String): Animatore =
        datasource.getAnimatore(foglio, animatore)


    suspend fun setDisponibilita(
        foglio: String,
        animatore: String,
        date: LocalDate,
        content: String
    ) =
        datasource.updateDisponibilita(
            foglio = foglio,
            animatore = animatore,
            date = date,
            content = content
        )

    fun getDomicilioAsFlow(foglio: String, animatore: String) =
        datasource.getDomicilioAsFlow(foglio = foglio, animatore = animatore)

    fun getNoteAsFlow(foglio: String, animatore: String) =
        datasource.getNoteAsFlow(foglio = foglio, animatore = animatore)

    fun getBambiniAsFlow(foglio: String, animatore: String) =
        datasource.getBambiniAsFlow(foglio = foglio, animatore = animatore)

    fun getAdultiAsFlow(foglio: String, animatore: String) =
        datasource.getAdultiAsFlow(foglio = foglio, animatore = animatore)

    fun getAutoAsFlow(foglio: String, animatore: String) =
        datasource.getAutoAsFlow(foglio = foglio, animatore = animatore)

    suspend fun updateDomicilio(foglio: String, animatore: String, value: String) =
        datasource.updateDomicilio(foglio, animatore, value)

    suspend fun updateAuto(foglio: String, animatore: String, value: Boolean) =
        datasource.updateAuto(foglio, animatore, value)

    suspend fun updateBambini(foglio: String, animatore: String, value: Boolean) =
        datasource.updateBambini(foglio, animatore, value)

    suspend fun updateAdulti(foglio: String, animatore: String, value: Boolean) =
        datasource.updateAdulti(foglio, animatore, value)

    suspend fun updateNote(foglio: String, animatore: String, value: String) =
        datasource.updateNote(foglio, animatore, value)

    suspend fun refreshAnimatore(foglio: String, animatore: String) {
        datasource.refreshAnimatore(foglio, animatore)
    }

    fun getDisponibilitaGiornaliere(foglio: String, day: LocalDate): DisponibilitaGiornaliere {
        val r = datasource.getFoglio(foglio).animatori.values.groupingBy {
            it.getTipoDisponibilita(day)
        }.eachCount()
        return DisponibilitaGiornaliere(
            r[DisponibilitaEnum.DISPONIBILE] ?: 0,
            r[DisponibilitaEnum.NON_DISPONIBILE] ?: 0,
            r[DisponibilitaEnum.ALTRO] ?: 0
        )
    }


}