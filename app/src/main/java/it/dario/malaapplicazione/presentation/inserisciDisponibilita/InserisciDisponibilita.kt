package it.dario.malaapplicazione.presentation.inserisciDisponibilita

import android.widget.CalendarView
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DateRangePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.asLiveData
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets.GiornoInserisci
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets.MalaCalendario
import it.dario.malaapplicazione.presentation.sharedComposable.MalaScaffold
import it.dario.malaapplicazione.presentation.sharedComposable.MalaSpinner
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.MalaViewModel
import java.time.ZoneId
import java.util.Calendar

@Composable
fun InserisciDisponibilita(
    viewModel: MalaViewModel,
    navigateUp: () -> Unit
) {
    MalaScaffold(
        label = stringResource(id = R.string.inserisci_disponibilita),
        navigateUp = navigateUp
    )
    { contentPadding ->
        Content(Modifier.padding(contentPadding), viewModel)
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    viewModel: MalaViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Spinner mese
        MalaSpinner(label = stringResource(id = R.string.seleziona_mese),
            options = viewModel.mesi,
            getOptionLabel = { it },
            onItemSelected = { it?.let { viewModel.selezionaMese(it) } })

        // Spinenr animatore
        val animatori = viewModel.animatori.asLiveData().observeAsState()
        animatori.value?.let {
            if (it.isNotEmpty()) {
                MalaSpinner(label = stringResource(id = R.string.seleziona_animatore),
                    options = it,
                    getOptionLabel = { it2 -> it2.label },
                    onItemSelected = { it2 -> it2?.let { it3 -> viewModel.selezionaAnimatore(it3.label) } })
            }
        }

        // Calendario Animatore
        val animatore = viewModel.disponibilitaAnimatore.asLiveData().observeAsState()
        animatore.value?.let {
            val foglio = viewModel.foglio
            MalaCalendario(
                viewModel = viewModel,
                startDate = foglio.primoGiorno,
                endDate = foglio.ultimoGiorno,
                dayContent = {
                    GiornoInserisci(
                        day = it,
                        value = animatore.value!!.getDisponibilita(it)
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewContent() {
    Content(
        viewModel = MalaViewModel(
            DisponibilitaRepository(
                MockDataSource()
            )
        )
    )
}



