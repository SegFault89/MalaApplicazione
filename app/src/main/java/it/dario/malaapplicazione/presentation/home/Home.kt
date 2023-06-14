package it.dario.malaapplicazione.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import it.dario.malaapplicazione.BuildConfig
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.MalaViewModel
import it.dario.malaapplicazione.presentation.home.widgets.HomeButton
import it.dario.malaapplicazione.presentation.sharedcomposable.BugReportIcon
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaProgressIndicator
import it.dario.malaapplicazione.presentation.theme.MarginBig
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.MarginSmall

/**
 * Pagina iniziale dell'app
 *
 * @param viewModel ViewModel per la schermata
 * @param onNavigateToInserisci funzione per navigare verso la pagina InserisciDisponibilita
 * @param onNavigateToVisualizza funzione per navigare verso la pagina VisualizzaDisponibilita
 * @param onNavigateToDatiFattura funzione per navigare verso la pagina DatiFattura
 * @param openBug funzione per navigare verso il dialog di apertura segnalazioni
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    viewModel: MalaViewModel,
    onNavigateToInserisci: () -> Unit = {},
    onNavigateToVisualizza: () -> Unit = {},
    onNavigateToDatiFattura: () -> Unit = {},
    openBug: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    BugReportIcon(openBug)
                }
            )
        }) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            //Indicatore di caricamento
            MalaProgressIndicator(Modifier.align(Alignment.TopEnd), viewModel.isReady)
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MarginBig),
            ) {
                val (buttons, footer) = createRefs()

                Buttons(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(buttons) {
                            top.linkTo(parent.top)
                            bottom.linkTo(footer.top)
                        },
                    viewModel = viewModel,
                    onNavigateToDatiFattura = onNavigateToDatiFattura,
                    onNavigateToInserisci = onNavigateToInserisci,
                    onNavigateToVisualizza = onNavigateToVisualizza
                )
                //Footer
                Footer(modifier = Modifier.constrainAs(footer){
                    bottom.linkTo(parent.bottom)
                })
            }
        }
    }
}

@Composable
fun Buttons(
    modifier: Modifier = Modifier,
    viewModel: MalaViewModel,
    onNavigateToInserisci: () -> Unit = {},
    onNavigateToVisualizza: () -> Unit = {},
    onNavigateToDatiFattura: () -> Unit = {},
) {
    val buttonModifier = Modifier.fillMaxWidth()
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        item {
            HomeButton(
                modifier = buttonModifier,
                label = stringResource(id = R.string.inserisci_disponibilita),
                description = stringResource(id = R.string.inserisci_disponibilita_description),
                onclick = onNavigateToInserisci,
                enabled = viewModel.isReady
            )
        }
        item {
            HomeButton(
                modifier = buttonModifier,
                label = stringResource(id = R.string.visualizza_disponibilita),
                description = stringResource(id = R.string.visualizza_disponibilita_description),
                onclick = onNavigateToVisualizza,
                enabled = viewModel.isReady
            )
        }
        item {
            HomeButton(
                modifier = buttonModifier,
                label = stringResource(id = R.string.dati_fattura),
                description = stringResource(id = R.string.dati_fattura_description),
                onclick = onNavigateToDatiFattura
            )
        }
    }
}

@Composable
fun Footer(modifier: Modifier) {
    Column(modifier = modifier) {
        //Avviso versione di test
        Text(
            text = stringResource(id = R.string.test_file),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = MarginNormal,
                    start = MarginNormal,
                    end = MarginNormal
                )
        )
        Text(
            text = stringResource(id = R.string.version).format(BuildConfig.VERSION_NAME),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MarginNormal,
                    end = MarginSmall
                )
        )
    }
}

@Preview(showSystemUi = true, device = "spec:width=300dp,height=841dp,dpi=480")
@Composable
fun Preview() {
    Home(viewModel = MalaViewModel(DisponibilitaRepository(MockDataSource())))
}
