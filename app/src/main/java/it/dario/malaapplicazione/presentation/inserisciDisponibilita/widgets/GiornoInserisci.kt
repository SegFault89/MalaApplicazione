package it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.data.Constants.DISPONIBILE
import it.dario.malaapplicazione.data.Constants.NON_DISPONIBILE
import it.dario.malaapplicazione.data.Constants.NO_DISPONIBILITA
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import it.dario.malaapplicazione.presentation.theme.DisponibileGreen
import it.dario.malaapplicazione.presentation.theme.DisponibileRed
import it.dario.malaapplicazione.presentation.theme.DisponibileYellow
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.InserisciDisponibilitaViewModel
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiornoInserisci(
    viewModel: InserisciDisponibilitaViewModel,
    day: LocalDate,
    foglio: Foglio,
    animatore: Animatore
) {

    val currentValue by animatore.getDisponibilitaAsFlow(day).collectAsState()

    Box(
        modifier = Modifier
            .aspectRatio(1f),
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(2.dp)
                .fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = when (currentValue) {
                    NON_DISPONIBILE -> DisponibileRed
                    DISPONIBILE -> DisponibileGreen
                    NO_DISPONIBILITA -> DisponibileYellow
                    else -> DisponibileYellow
                },
            ),
            onClick = {
                viewModel.updateAnimatoreDisponibilita(
                    animatore,
                    foglio,
                    day,
                    if (currentValue == DISPONIBILE) NON_DISPONIBILE else DISPONIBILE
                )
            }
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                //color = if (enabled) Color.Black else Color.LightGray,
            )
        }
    }
}