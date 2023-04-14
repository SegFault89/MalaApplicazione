package it.dario.malaapplicazione.presentation.visualizzaDisponibilita

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.presentation.sharedComposable.MalaScaffold

@Composable
fun VisualizzaDisponibilita(navigateUp: ()-> Unit) {
    MalaScaffold(label = stringResource(id = R.string.visualizza_disponibilita), navigateUp = navigateUp )
    { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.visualizza_disponibilita))
        }
    }
}

