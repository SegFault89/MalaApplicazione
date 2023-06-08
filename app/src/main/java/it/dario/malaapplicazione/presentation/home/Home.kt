package it.dario.malaapplicazione.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.MalaViewModel
import it.dario.malaapplicazione.presentation.home.widgets.HomeButton
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaProgressIndicator
import it.dario.malaapplicazione.presentation.theme.MalaApplicazioneTheme

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
    onNavigateToDatiFattura: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        }) { contentPadding ->
        Row(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        )
        {

            Box {
                MalaProgressIndicator(Modifier.align(Alignment.TopEnd), viewModel.isReady)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(IntrinsicSize.Max),
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
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HomeLightPreview() {
    MalaApplicazioneTheme {
        Home(viewModel = MalaViewModel(DisponibilitaRepository(MockDataSource())))
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeDarkPreview() {
    MalaApplicazioneTheme {
        Home(viewModel = MalaViewModel(DisponibilitaRepository(MockDataSource())))
    }
}