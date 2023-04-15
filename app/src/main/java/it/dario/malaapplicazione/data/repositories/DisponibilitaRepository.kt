package it.dario.malaapplicazione.data.repositories

import it.dario.malaapplicazione.data.datasources.IDisponibilitaDataSource

class DisponibilitaRepository (val datasource: IDisponibilitaDataSource) {

    fun getMesi() : List<String>{
        return datasource.getMesi()
    }
}