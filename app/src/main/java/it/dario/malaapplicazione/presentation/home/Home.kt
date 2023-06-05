package it.dario.malaapplicazione.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.home.widgets.HomeButton
import it.dario.malaapplicazione.presentation.theme.MalaApplicazioneTheme
import it.dario.malaapplicazione.presentation.MalaViewModel

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