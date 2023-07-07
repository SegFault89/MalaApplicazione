package it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.data.Constants.DISPONIBILE
import it.dario.malaapplicazione.data.Constants.NON_DISPONIBILE
import it.dario.malaapplicazione.data.Constants.NO_DISPONIBILITA
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.InserisciDisponibilitaViewModel
import it.dario.malaapplicazione.presentation.theme.DisponibileGreen
import it.dario.malaapplicazione.presentation.theme.DisponibileRed
import it.dario.malaapplicazione.presentation.theme.DisponibileYellow
import it.dario.malaapplicazione.presentation.theme.TextOverColor
import java.time.LocalDate

/**
 * Cella del giorno per il calendario nella schermata "InserisciDisponibilita"
 *
 * @param viewModel viewmodel di riferimento con i dati
 * @param day giorno di riferimento per la cella
 * @param foglio nome del foglio di riferimento per il recupero dati
 * @param animatore animatore di riferimento per il recupero dei dati
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GiornoInserisci(
    viewModel: InserisciDisponibilitaViewModel,
    day: LocalDate,
    foglio: String,
    animatore: String
) {

    Log.d(Constants.TAG, "composing GiornoInserisci for $day")
    val currentValue by viewModel.getDisponibilitaAsFlow(foglio, animatore, day).collectAsState()

    var openDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .aspectRatio(1f),
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(2.dp)
                .fillMaxSize()
                .combinedClickable(
                    onClick = {
                        //se l'animatore è in stato disponibile o non disponibile, cambio lo stato
                        if (currentValue in listOf(DISPONIBILE, NON_DISPONIBILE)) {
                            viewModel.updateAnimatoreDisponibilita(
                                foglio,
                                animatore,
                                day,
                                if (currentValue == DISPONIBILE) NON_DISPONIBILE else DISPONIBILE
                            )
                        } else {
                            //altrimenti apro la modale per l'inserimento
                            openDialog = true
                        }
                    },
                    onLongClick = { openDialog = true }
                ),
            colors = CardDefaults.cardColors(
                containerColor = when (currentValue) {
                    NON_DISPONIBILE -> DisponibileRed
                    DISPONIBILE -> DisponibileGreen
                    NO_DISPONIBILITA -> DisponibileYellow
                    else -> DisponibileYellow
                },
            ),
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = TextOverColor
            )
        }
    }

    // Sheet content
    if (openDialog) {
        InsertDisponibilitaDialog(
            day = day,
            disponibilita = currentValue,
            saveValue = {
                viewModel.updateAnimatoreDisponibilita(
                    foglio,
                    animatore,
                    day, it
                )
            },
            dismiss = { openDialog = false }
        )
    }
}
