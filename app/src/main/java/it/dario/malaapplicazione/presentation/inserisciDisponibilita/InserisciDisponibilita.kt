package it.dario.malaapplicazione.presentation.inserisciDisponibilita

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.presentation.sharedComposable.MalaScaffold

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
            Content()
        }
    }
}

@Composable
fun Content(){
    Column(modifier = Modifier.fillMaxSize()) {
        //MalaSpinner()
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewContent() {
    Content()
}