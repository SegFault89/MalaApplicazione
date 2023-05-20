package it.dario.malaapplicazione.data.repositories

import it.dario.malaapplicazione.data.datasources.IDisponibilitaDataSource
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio

class DisponibilitaRepository (val datasource: IDisponibilitaDataSource) {

    fun getMesi() : List<String>{
        return datasource.getMesi()
    }

    fun getFoglio(nome: String) : Foglio {
        return datasource.getFoglio(nome)
    }

    fun getAnimatori(foglio: String) : List<Animatore> {
        return datasource.getAnimatori(foglio)
    }

    fun getAnimatore(animatore: String) : Animatore? {
        return datasource.getAnimatore(animatore)
    }
}