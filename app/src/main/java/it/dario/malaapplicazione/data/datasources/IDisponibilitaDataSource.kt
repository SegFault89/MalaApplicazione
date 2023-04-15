package it.dario.malaapplicazione.data.datasources

sealed interface IDisponibilitaDataSource {
    fun getMesi() : List<String>
}