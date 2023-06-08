package it.dario.malaapplicazione.data.datasources

interface IDatasourceErrorHandler {

    fun onGetFogliError(t: Throwable)
    fun onGetAnimatoriError(t: Throwable)
    fun onGetAnimatoriCompleteError(t: Throwable)
    fun onGetGiorniError(t: Throwable)
    fun onGetAnimatoreError(t: Throwable)
    fun onUpdateCellError(t: Throwable)

}