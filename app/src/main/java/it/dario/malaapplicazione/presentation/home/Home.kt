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
import it.dario.malaapplicazione.presentation.home.widgets.HomeButton
import it.dario.malaapplicazione.presentation.theme.MalaApplicazioneTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onNavigateToInserisci: () -> Unit = {},
    onNavigateToVisualizza: () -> Unit = {},
    onNavigateToDatiFattura: () -> Unit = {},
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
                    onclick = onNavigateToInserisci
                )
                HomeButton(
                    modifier = buttonModifier,
                    label = stringResource(id = R.string.visualizza_disponibilita),
                    onclick = onNavigateToVisualizza
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
        Home()
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeDarkPreview() {
    MalaApplicazioneTheme {
        Home()
    }
}