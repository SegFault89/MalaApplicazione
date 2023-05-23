package it.dario.malaapplicazione.presentation.inserisciDisponibilita

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets.GiornoInserisci
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets.MalaCalendario
import it.dario.malaapplicazione.presentation.sharedComposable.LabeledCheckbox
import it.dario.malaapplicazione.presentation.sharedComposable.MalaScaffold
import it.dario.malaapplicazione.presentation.sharedComposable.MalaSpinner
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.VerticalSpacingNormal
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

val spinnerModifier = Modifier
    .fillMaxWidth()
    .padding(bottom = VerticalSpacingNormal, start = MarginNormal, end = MarginNormal)

@Composable
fun Content(
    modifier: Modifier = Modifier,
    viewModel: InserisciDisponibilitaViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MarginNormal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val currentState by viewModel.uiState.collectAsState()

        // Spinner foglio
        MalaSpinner(
            modifier = spinnerModifier,
            label = stringResource(id = R.string.seleziona_foglio),
            options = viewModel.mesi,
            getOptionLabel = { it },
            selected = currentState.foglioSelezionato,
            onItemSelected = viewModel::updateFoglioSelezionato
        )

        //spinner animatore
        currentState.foglioSelezionato?.let {foglio ->
            if (foglio.isNotEmpty()) {
                MalaSpinner<Animatore>(
                    modifier = spinnerModifier,
                    label = stringResource(id = R.string.seleziona_animatore),
                    options = viewModel.getAnimatoriInFoglio(foglio),
                    getOptionLabel = { animatore -> animatore.label },
                    onItemSelected = { viewModel.updateAnimatoreSelezionato(it.label) },
                    selected = currentState.animatoreSelezionato
                )

                currentState.animatoreSelezionato?.let { animatore ->
                    AnimatoreData(viewModel = viewModel, animatore = animatore, foglio = foglio )
                }
            }
        }
    }
}

@Composable
fun AnimatoreData(
    viewModel: InserisciDisponibilitaViewModel,
    animatore: String,
    foglio: String,
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        viewModel.getFoglio(foglio).let { malaFoglio ->
            MalaCalendario(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = VerticalSpacingNormal),
                startDate = malaFoglio.primoGiorno,
                endDate = malaFoglio.ultimoGiorno,
                dayContent = {
                    GiornoInserisci(
                        viewModel = viewModel,
                        day = it,
                        foglio = malaFoglio.label,
                        animatore = animatore
                    )
                }
            )
        }

        viewModel.getAnimatore(foglio, animatore).let { malaAnimatore ->

            // TODO SISTEMARE PASSANDO PER VIEWMODEL E NON DI ANIMATORE DIRETTO
            val domicilioState by malaAnimatore.getDomicilioAsFlow().collectAsState()
            val noteState by malaAnimatore.getNoteAsFlow().collectAsState()

            val autoState by malaAnimatore.getAutoAsFlow().collectAsState()
            val bambiniState by malaAnimatore.getBambiniAsFlow().collectAsState()
            val adultiState by malaAnimatore.getAdultiAsFlow().collectAsState()

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = domicilioState,
                onValueChange = { viewModel.updateDomicilio(foglio, animatore, it)},
                label = { Text("DOMICILIO") },
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = noteState,
                onValueChange = { viewModel.updateNote(foglio, animatore, it) },
                label = { Text("NOTE") },
            )

            LabeledCheckbox(
                checked = autoState,
                label = "AUTO",
                onCheckedChanged = { viewModel.updateAuto(foglio, animatore, it) }
            )

            LabeledCheckbox(
                checked = adultiState,
                label = "ADULTI",
                onCheckedChanged = { viewModel.updateAdulti(foglio, animatore, it) }
            )

            LabeledCheckbox(
                checked = bambiniState,
                label = "BAMBINI",
                onCheckedChanged = { viewModel.updateBambini(foglio, animatore, it) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewContent() {
    val mock = MockDataSource()
    Content(
        viewModel = InserisciDisponibilitaViewModel(
            DisponibilitaRepository(
                mock
            )
        ).apply {
            updateFoglioSelezionato(mock.foglioNovembre.label)
            updateAnimatoreSelezionato(mock.darioNovembre.label)
        }
    )
}



