package it.dario.malaapplicazione.presentation.datifattura

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaScaffold
import it.dario.malaapplicazione.presentation.theme.MarginNormal

/**
 * Lista dei valori che saranno messi nella griglia presente nella schermata
 */
val gridItems = listOf(
    R.string.naziionalita,
    R.string.naziionalita_value,
    R.string.codice_fiscale,
    R.string.codice_fiscale_value,
    R.string.partita_iva,
    R.string.partita_iva_value,
    R.string.denominazione,
    R.string.denominazione_value,
    R.string.indirizzo,
    R.string.indirizzo_value,
    R.string.codiceDestinatario,
    R.string.codiceDestinatario_value
)

/**
 * Mostra uno schermo con un qr code contenente i dati per la fattura
 * e una griglia con i dati in formato testuale
 */
@Preview(showBackground = true)
@Composable
fun DatiFattura(navigateUp: () -> Unit = {}) {
    MalaScaffold(
        label = stringResource(id = R.string.dati_fattura),
        navigateUp = navigateUp
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.qr_fattura),
                contentDescription = stringResource(id = R.string.qr_content_descriptor),
                contentScale = ContentScale.Fit,
                modifier = Modifier.weight(1f)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).padding(MarginNormal),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(gridItems) {
                    Text(text = stringResource(id = it))
                }
            }
        }
    }
}
