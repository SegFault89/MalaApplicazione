package it.dario.malaapplicazione.presentation.inserisciDisponibilita

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.asLiveData
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets.GiornoInserisci
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets.MalaCalendario
import it.dario.malaapplicazione.presentation.sharedComposable.MalaScaffold
import it.dario.malaapplicazione.presentation.sharedComposable.MalaSpinner
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.InserisciDisponibilitaViewModel

@Composable
fun InserisciDisponibilita(
    viewModel: InserisciDisponibilitaViewModel,
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
    viewModel: InserisciDisponibilitaViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val currentState by viewModel.uiState.collectAsState()

        // Spinner foglio
        MalaSpinner(
            label = stringResource(id = R.string.seleziona_foglio),
            options = viewModel.mesi,
            getOptionLabel = { it },
            selected = currentState.foglioSelezionato,
            onItemSelected = viewModel::updateFoglioSelezionato
        )

        //spinner animatore
        currentState.foglioSelezionato?.let {
            if (it.isNotEmpty()) {
                MalaSpinner(
                    label = stringResource(id = R.string.seleziona_animatore),
                    options = viewModel.getAnimatoriInFoglio(it),
                    getOptionLabel = { animatore -> animatore.label },
                    onItemSelected = viewModel::updateAnimatoreSelezionato,
                    selected = currentState.animatoreSelezionato
                )
            }
        }

        currentState.animatoreSelezionato?.let {animatore ->
            val foglio = viewModel.getFoglio(currentState.foglioSelezionato!!) // TODO not null check giusto per essere sicuri?
            MalaCalendario(
                startDate = foglio.primoGiorno,
                endDate = foglio.ultimoGiorno,
                dayContent = {
                    GiornoInserisci(
                        viewModel= viewModel,
                        day = it,
                        foglio = foglio,
                        animatore = animatore
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
        viewModel = InserisciDisponibilitaViewModel(
            DisponibilitaRepository(
                MockDataSource()
            )
        )
    )
}



