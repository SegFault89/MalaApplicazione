package it.dario.malaapplicazione.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import it.dario.malaapplicazione.BuildConfig
import it.dario.malaapplicazione.R
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
            //Footer
            Footer(modifier = Modifier.align(Alignment.BottomCenter))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .width(IntrinsicSize.Max)
                    .padding(MarginBig),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                val buttonModifier = Modifier.fillMaxWidth()
                HomeButton(
                    modifier = buttonModifier,
                    label = stringResource(id = R.string.inserisci_disponibilita),
                    onclick = onNavigateToInserisci,
                    enabled = viewModel.isReady
                )
                HomeButton(
                    modifier = buttonModifier,
                    label = stringResource(id = R.string.visualizza_disponibilita),
                    onclick = onNavigateToVisualizza,
                    enabled = viewModel.isReady
                )
                HomeButton(
                    modifier = buttonModifier,
                    label = stringResource(id = R.string.dati_fattura),
                    onclick = onNavigateToDatiFattura
                )
            }
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
                    bottom = MarginNormal,
                    start = MarginNormal,
                    end = MarginSmall
                )
        )
    }
}
