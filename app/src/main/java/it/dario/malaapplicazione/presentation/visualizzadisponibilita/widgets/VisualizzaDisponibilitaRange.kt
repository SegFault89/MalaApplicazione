package it.dario.malaapplicazione.presentation.visualizzadisponibilita.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.domain.utils.rangeTo
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets.MalaCalendario
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.VerticalSpacingNormal
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilitaViewModel
import java.time.LocalDate

@Composable
fun VisualizzaDisponibilitaRange(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglio: String
) {
    val openDialogStart = remember { mutableStateOf(false) }
    val firstDaySelected by viewModel.primoGiornoSelezionato.collectAsState()
    val lastDaySelected by viewModel.ultimoGiornoSelezionato.collectAsState()

    if (openDialogStart.value) {
        SelectRangeDialog(
            viewModel = viewModel,
            foglio = foglio,
            onDismissRequest = { openDialogStart.value = false }
        )
    }
    OutlinedButton(
        onClick = { openDialogStart.value = true },
        modifier = Modifier.padding(top= MarginNormal).fillMaxWidth()
    ) {
        Text(modifier = Modifier.weight(.45f),
            text = firstDaySelected?.let {firstDaySelected.toString() } ?: stringResource(id = R.string.dal),
            textAlign = TextAlign.Center)

        Text(modifier = Modifier.weight(.1f),
            text = "|",
            textAlign = TextAlign.Center)

        Text(modifier = Modifier.weight(.45f),
            text = lastDaySelected?.let {lastDaySelected.toString() } ?: stringResource(id = R.string.al),
            textAlign = TextAlign.Center)
    }
    if (firstDaySelected != null && lastDaySelected != null && !openDialogStart.value) {
        val range = firstDaySelected!!.rangeTo(lastDaySelected!!).toList()

        AnimatoriRangeList(viewModel, foglio, range)
    }
}

@Composable
fun AnimatoriRangeList(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglioSelezionato: String,
    days: List<LocalDate>
) {

        val lst = viewModel.getAnimatoriDisponibiliRange(foglioSelezionato, days).toList()

        LazyColumn(
            modifier = Modifier.padding(MarginNormal),
            verticalArrangement = Arrangement.spacedBy(VerticalSpacingNormal),
            state = LazyListState(0)
        ) {
            items(lst, key = { (it.id * 100) + days.first().dayOfMonth }) {
                AnimatoreRangeListItem(
                    animatore = it,
                     giorni = days
                )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRangeDialog(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglio: String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(MarginNormal)) {
                MalaCalendario(
                    startDate = viewModel.getPrimoGiorno(foglio),
                    endDate = viewModel.getUltimoGiorno(foglio),
                    dayContent = {
                        GiornoVisualizzaRange(viewModel = viewModel, day = it)
                    }
                )
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(alignment = Alignment.End)
                ) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
            }
        }
    }
}
