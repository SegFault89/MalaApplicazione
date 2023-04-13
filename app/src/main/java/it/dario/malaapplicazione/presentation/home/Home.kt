package it.dario.malaapplicazione.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import it.dario.malaapplicazione.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onNavigateToInserisci: () -> Unit,
    onNavigateToVisualizza: () -> Unit,
    onNavigateToDatiFattura: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        }) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = onNavigateToInserisci) {
                Text(text = stringResource(id = R.string.inserisci_disponibilita))
            }
            Button(onClick = onNavigateToVisualizza) {
                Text(text = stringResource(id = R.string.visualizza_disponibilita))
            }
            Button(onClick = onNavigateToDatiFattura) {
                Text(text = stringResource(id = R.string.dati_fattura))
            }
        }
    }
}