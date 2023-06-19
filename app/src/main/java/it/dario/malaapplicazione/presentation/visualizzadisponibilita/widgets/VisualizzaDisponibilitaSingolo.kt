package it.dario.malaapplicazione.presentation.visualizzadisponibilita.widgets

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets.MalaCalendario
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.VerticalSpacingNormal
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilitaViewModel

@Composable
fun VisualizzaDisponibilitaSingolo(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglio: String,
) {

    Log.d(Constants.TAG, "composing VisualizzaDisponibilitaSingolo")

    MalaCalendario(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = VerticalSpacingNormal),
        startDate = viewModel.getPrimoGiorno(foglio),
        endDate = viewModel.getUltimoGiorno(foglio),
        dayContent = {
            GiornoVisualizza(
                viewModel = viewModel,
                day = it,
                foglio = foglio,
            )
        }
    )

    AnimatoriList(viewModel = viewModel, foglio)

}


@Composable
fun AnimatoriList(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglioSelezionato: String
) {
    val giornoSelezionato by viewModel.giornoSelezionato.collectAsState()


    giornoSelezionato?.let { giorno ->
        val lst = viewModel.getAnimatoriDisponibili(foglioSelezionato, giorno).toList()

        LazyColumn(
            modifier = Modifier.padding(MarginNormal),
            verticalArrangement = Arrangement.spacedBy(VerticalSpacingNormal),
            state = LazyListState(0)
        ) {
            items(lst, key = { (it.id * 100) + giorno.dayOfMonth }) {
                AnimatoreListItem(
                    animatore = it,
                    giornoSelezionato = giorno
                )
            }
        }
    }
}