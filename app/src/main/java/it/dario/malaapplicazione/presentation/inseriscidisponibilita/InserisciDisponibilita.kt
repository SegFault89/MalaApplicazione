package it.dario.malaapplicazione.presentation.inseriscidisponibilita

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets.BottomSheetLegenda
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets.GiornoInserisci
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets.MalaCalendario
import it.dario.malaapplicazione.presentation.sharedcomposable.LabeledCheckbox
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaOutlinedTextBox
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaScaffold
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaSpinner
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.VerticalSpacingNormal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserisciDisponibilita(
    viewModel: InserisciDisponibilitaViewModel,
    navigateUp: () -> Unit,
    openBug: () -> Unit = {}
) {
    MalaScaffold(
        label = stringResource(id = R.string.inserisci_disponibilita),
        navigateUp = navigateUp,
        openBug = openBug,
        additionalAction = listOf {
            LegendaButton()
        }
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

    val currentFoglio by viewModel.foglioSelezionato.collectAsState()
    val currentAnimatore by viewModel.animatoreSelezionato.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MarginNormal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            SpinnerSection(
                viewModel = viewModel,
                foglio = currentFoglio,
                animatore = currentAnimatore
            )
        }
        if (currentFoglio != null && currentAnimatore != null) {
            item {
                AnimatoreData(
                    viewModel = viewModel,
                    foglio = currentFoglio!!,
                    animatore = currentAnimatore!!
                )
            }
        }
    }
}

@Composable
fun SpinnerSection(
    viewModel: InserisciDisponibilitaViewModel,
    foglio: String?,
    animatore: String?,
) {

    val foglioLoading by viewModel.loadingFoglio.collectAsState()

    // Spinner foglio
    MalaSpinner(
        modifier = spinnerModifier,
        label = stringResource(id = R.string.seleziona_foglio),
        options = viewModel.getFogli(),
        getOptionLabel = { it },
        selected = foglio,
        onItemSelected = viewModel::updateFoglioSelezionato
    )

    //spinner animatore
    if (!foglio.isNullOrEmpty()) {
        if (foglioLoading) {
            //se sto caricando
            CircularProgressIndicator()
        } else {
            //se ho caricato
            MalaSpinner(
                modifier = spinnerModifier,
                label = stringResource(id = R.string.seleziona_animatore),
                options = viewModel.listAnimatori,
                getOptionLabel = { a -> a.label },
                onItemSelected = { viewModel.updateAnimatoreSelezionato(it?.label) },
                selected = animatore,
                searchable = true
            )
        }
    }
}

@Composable
fun AnimatoreData(
    viewModel: InserisciDisponibilitaViewModel,
    animatore: String,
    foglio: String,
) {

    Log.d(Constants.TAG, "composing AnimatoreData")
    val animatoreLoading by viewModel.loadingAnimatore.collectAsState()
    if (animatoreLoading) {
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
                GiornoInserisci(
                    viewModel = viewModel,
                    day = it,
                    foglio = foglio,
                    animatore = animatore
                )
            }
        )


        MalaOutlinedTextBox(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.domicilio),
            toObserve = viewModel.getDomicilioAsFlow(foglio, animatore),
            onValueChangeListener = { viewModel.updateDomicilio(foglio, animatore, it) })

        MalaOutlinedTextBox(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.note),
            toObserve = viewModel.getNoteAsFlow(foglio, animatore),
            onValueChangeListener = { viewModel.updateNote(foglio, animatore, it) })

        LabeledCheckbox(
            toObserve = viewModel.getAutoAsFlow(foglio, animatore),
            label = stringResource(id = R.string.auto),
            onCheckedChanged = { viewModel.updateAuto(foglio, animatore, it) }
        )

        LabeledCheckbox(
            label = stringResource(id = R.string.adulti),
            onCheckedChanged = { viewModel.updateAdulti(foglio, animatore, it) },
            toObserve = viewModel.getAdultiAsFlow(foglio, animatore)
        )

        LabeledCheckbox(
            label = stringResource(id = R.string.bambini),
            onCheckedChanged = { viewModel.updateBambini(foglio, animatore, it) },
            toObserve = viewModel.getBambiniAsFlow(foglio, animatore)
        )
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegendaButton(
) {

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    IconButton(onClick = {openBottomSheet = true}) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_info_24),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(id = R.string.legenda)
        )
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            BottomSheetLegenda()
        }
    }
}
