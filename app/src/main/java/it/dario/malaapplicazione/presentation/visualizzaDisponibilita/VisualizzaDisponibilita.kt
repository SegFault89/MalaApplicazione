package it.dario.malaapplicazione.presentation.visualizzaDisponibilita

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets.MalaCalendario
import it.dario.malaapplicazione.presentation.sharedComposable.MalaScaffold
import it.dario.malaapplicazione.presentation.sharedComposable.MalaSpinner
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.Others.spinnerModifier
import it.dario.malaapplicazione.presentation.theme.VerticalSpacingNormal
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.widgets.GiornoVisualizza

@Composable
fun VisualizzaDisponibilita(
    viewModel: VisualizzaDisponibilitaViewModel,
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
    viewModel: VisualizzaDisponibilitaViewModel
) {

    val currentFoglio by viewModel.foglioSelezionato.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MarginNormal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpinnerSection(
            viewModel = viewModel,
            foglio = currentFoglio,
        )

    if (currentFoglio != null) {
        DisponibilitaData(
            viewModel = viewModel,
            foglio = currentFoglio!!,
        )
        }
    }
}

@Composable
fun SpinnerSection(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglio: String?,
) {

    // Spinner foglio
    MalaSpinner(
        modifier = spinnerModifier,
        label = stringResource(id = R.string.seleziona_foglio),
        options = viewModel.mesi,
        getOptionLabel = { it },
        selected = foglio,
        onItemSelected = viewModel::updateFoglioSelezionato
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisponibilitaData(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglio: String,
) {

    Log.d(Constants.TAG, "composing DisponibilitaData")
    val foglioIsLoading by viewModel.loadingFoglio.collectAsState()
    if (foglioIsLoading) {
        //se sto caricando
        CircularProgressIndicator()
    } else {
        //Se ho caricato
        MalaCalendario(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = VerticalSpacingNormal),
            startDate = viewModel.getPrimoGiorno(foglio),
            endDate = viewModel.getUltimoGiorno(foglio),
            dayContent = {
                GiornoVisualizza(
                    viewModel = viewModel,
                    day = it,
                    foglio = foglio,
                    onDaySelected = {} //TODO
                )
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewContent() {
    val mock = MockDataSource()
    Content(
        viewModel = VisualizzaDisponibilitaViewModel(
            DisponibilitaRepository(
                mock
            )
        ).apply {
            updateFoglioSelezionato(mock.foglioNovembre.label)
        }
    )
}



