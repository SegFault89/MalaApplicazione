package it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.presentation.theme.BigSpacer
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


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
            Column(
                modifier = Modifier.padding(MarginNormal),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    day.format(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                            .withLocale(Locale.getDefault())
                    )
                )

                Spacer(modifier = Modifier.height(BigSpacer))

                var value by remember { mutableStateOf(disponibilita) }

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text(text = stringResource(id = R.string.inserisci_disponibilita_dialog)) },
                )

                Spacer(modifier = Modifier.height(BigSpacer))
                TextButton(
                    onClick = { saveValue(value); dismiss() },
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