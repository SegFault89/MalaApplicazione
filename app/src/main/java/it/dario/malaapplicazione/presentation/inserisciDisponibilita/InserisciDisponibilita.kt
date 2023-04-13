package it.dario.malaapplicazione.presentation.inserisciDisponibilita

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.presentation.sharedComposable.GoBack
import it.dario.malaapplicazione.presentation.sharedComposable.MalaScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserisciDisponibilita(navigateUp: () -> Unit) {
    MalaScaffold(
        label = stringResource(id = R.string.inserisci_disponibilita),
        navigateUp = navigateUp
    )
    { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.inserisci_disponibilita))
        }
    }
}