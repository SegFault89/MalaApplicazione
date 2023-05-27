package it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.data.Constants.DISPONIBILE
import it.dario.malaapplicazione.data.Constants.NON_DISPONIBILE
import it.dario.malaapplicazione.data.Constants.NO_DISPONIBILITA
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.presentation.theme.DisponibileGreen
import it.dario.malaapplicazione.presentation.theme.DisponibileRed
import it.dario.malaapplicazione.presentation.theme.DisponibileOrange
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.InserisciDisponibilitaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


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
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
                    NO_DISPONIBILITA -> DisponibileOrange
                    else -> DisponibileOrange
                },
            ),
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    // Sheet content
    if (openDialog) {
        var newDisponibilita = currentValue

        CustomAlertDialog(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAlertDialog(
    day: LocalDate,
    disponibilita: String,
    saveValue: (String) -> Unit,
    dismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = dismiss) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    day.format(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                            .withLocale(Locale.getDefault())
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                var value by remember { mutableStateOf(disponibilita) }

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text(stringResource(id = R.string.inserisci_disponibilita_dialog)) },
                )

                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {saveValue(value); dismiss() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    val mock = MockDataSource()
    CustomAlertDialog(
        day = mock.foglioNovembre.ultimoGiorno,
        disponibilita = "dalle 18:00",
        saveValue = {},
        dismiss = {}
    )
}