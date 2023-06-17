package it.dario.malaapplicazione.presentation.visualizzadisponibilita.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets.MalaCalendario
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilitaViewModel

@Composable
fun VisualizzaDisponibilitaRange(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglio: String
) {
    val openDialogStart = remember { mutableStateOf(false) }

    if (openDialogStart.value) {
        SelectRangeDialog(
            viewModel = viewModel,
            foglio = foglio,
            onDismissRequest = { openDialogStart.value = false }
        )
    }
    OutlinedButton(onClick = { openDialogStart.value = true }) {

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
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Dismiss")
                }
            }
        }
    }
}
