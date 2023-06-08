package it.dario.malaapplicazione.domain

import it.dario.malaapplicazione.data.datasources.IDatasourceErrorHandler

class DatasourceErrorHandler : IDatasourceErrorHandler{
    override fun onGetFogliError(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onGetAnimatoriError(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onGetAnimatoriCompleteError(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onGetGiorniError(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onGetAnimatoreError(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onUpdateCellError(t: Throwable) {
        TODO("Not yet implemented")
    }
}