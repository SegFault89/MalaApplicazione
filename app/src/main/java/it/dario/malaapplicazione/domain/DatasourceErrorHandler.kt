package it.dario.malaapplicazione.domain

import com.google.firebase.crashlytics.FirebaseCrashlytics
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.datasources.IDatasourceErrorHandler

class DatasourceErrorHandler(val showMessage : (stringId: Int) -> Unit) : IDatasourceErrorHandler{
    override fun onGetFogliError(t: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(t)
        showMessage(R.string.get_fogli_error)
    }

    override fun onGetAnimatoriError(t: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(t)
        showMessage(R.string.get_animatori_error)
    }

    override fun onGetAnimatoriCompleteError(t: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(t)
        showMessage(R.string.get_animatori_complete_error)
    }

    override fun onGetGiorniError(t: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(t)
        showMessage(R.string.get_giorni_error)
    }

    override fun onGetAnimatoreError(t: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(t)
        showMessage(R.string.get_animatore_error)
    }

    override fun onUpdateCellError(t: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(t)
        showMessage(R.string.update_cell_error)
    }
}
