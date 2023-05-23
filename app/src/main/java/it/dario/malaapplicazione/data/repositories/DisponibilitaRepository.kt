package it.dario.malaapplicazione.data.repositories

import it.dario.malaapplicazione.data.datasources.IDisponibilitaDataSource
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import java.time.LocalDate

class DisponibilitaRepository (val datasource: IDisponibilitaDataSource) {
    fun getFogli() : List<String>{
        return datasource.getFogli()
    }

    fun getFoglio(nome: String) : Foglio {
        return datasource.getFoglio(nome)
    }

    fun getAnimatori(foglio: String) : List<Animatore> {
        return datasource.getAnimatori(foglio)
    }

    fun getDisponibilitaAsFlow(foglio: String, animatore: String, date: LocalDate) =
        datasource.getDisponibilitaAsFlow(foglio = foglio, animatore = animatore, date= date)

    fun getAnimatore(foglio: String, animatore: String): Animatore =
        datasource.getAnimatore(foglio, animatore)


    fun setDisponibilita(foglio: String, animatore: String, date: LocalDate, content: String) =
        datasource.setDisponibilita(foglio = foglio, animatore = animatore, date = date, content = content)

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
    fun updateDomicilio (foglio: String, animatore: String, value: String) = datasource.updateDomicilio(foglio, animatore, value)
    fun updateAuto(foglio: String, animatore: String, value: Boolean) = datasource.updateAuto(foglio, animatore, value)
    fun updateBambini (foglio: String, animatore: String, value: Boolean) = datasource.updateBambini(foglio, animatore, value)
    fun updateAdulti (foglio: String, animatore: String, value: Boolean) = datasource.updateAdulti(foglio, animatore, value)
    fun updateNote (foglio: String, animatore: String, value: String) = datasource.updateNote(foglio, animatore, value)

}