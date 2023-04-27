package it.dario.malaapplicazione.data.repositories

import it.dario.malaapplicazione.data.datasources.IDisponibilitaDataSource
import it.dario.malaapplicazione.data.model.Animatore

class DisponibilitaRepository (val datasource: IDisponibilitaDataSource) {

    fun getMesi() : List<String>{
        return datasource.getMesi()
    }

    fun getAnimatori(mese: String) : List<Animatore> {
        return datasource.getAnimatori(mese)
    }

    fun getAnimatore(animatore: String) : Animatore? {
        return datasource.getAnimatore(animatore)
    }
}