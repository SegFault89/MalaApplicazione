package it.dario.malaapplicazione.presentation.visualizzadisponibilita.widgets

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets.MalaCalendario
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.VerticalSpacingNormal
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilitaViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun VisualizzaDisponibilitaSingolo(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglio: String,
) {

    val giornoSelezionato by viewModel.giornoSelezionato.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Log.d(Constants.TAG, "composing VisualizzaDisponibilitaSingolo")

    val lazyListState = rememberLazyListState()

    val showDate by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }


    AnimatedVisibility(visible = showDate, modifier = Modifier.fillMaxWidth()) {
        giornoSelezionato?.let { day ->
        OutlinedButton(
            onClick = { coroutineScope.launch { lazyListState.animateScrollToItem(0) }},
            modifier = Modifier.padding(top= MarginNormal).fillMaxWidth()
        ) {

                Text(
                    //text = SimpleDateFormat("EE dd MMMM", Locale.current).format(giornoSelezionato),
                    text = day.format(DateTimeFormatter.ofPattern("EEEE dd MMMM")),
                    textAlign = TextAlign.Center)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MarginNormal),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = lazyListState,
    ) {

        item {
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
        }

        giornoSelezionato?.let {
            item(key = giornoSelezionato) { AnimatoriList(viewModel = viewModel, foglio, it) }
        }
    }

}


@Composable
fun AnimatoriList(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglioSelezionato: String,
    giornoSelezionato: LocalDate
) {


    giornoSelezionato.let { giorno ->
        val lst = viewModel.getAnimatoriDisponibili(foglioSelezionato, giorno).toList()

        Column(
            modifier = Modifier.padding(MarginNormal),
            verticalArrangement = Arrangement.spacedBy(VerticalSpacingNormal),
        ) {
            lst.map {
                AnimatoreListItem(
                    animatore = it,
                    giornoSelezionato = giorno
                )
            }
        }
    }
}
