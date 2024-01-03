package it.dario.malaapplicazione.presentation.visualizzadisponibilita.widgets

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.data.Constants.DISPONIBILE
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.VerticalSpacingSmall
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatoreListItem(
    animatore: Animatore,
    giornoSelezionato: LocalDate
) {

    Log.d(Constants.TAG, "composing AnimatoreListItem for ${animatore.label}")

    var expanded by remember { mutableStateOf (false) }

    val disponibilita = animatore.getDisponibilita(giornoSelezionato)

    Card(
        onClick = {expanded = !expanded},
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (expanded) {
                MaterialTheme.colorScheme.primaryContainer
            }
            else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(MarginNormal)) {
            Text(
                text =  buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.bodyLarge.toSpanStyle()) {
                        append(animatore.label)
                    }
                    if (disponibilita != DISPONIBILE) {
                        withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                            append(" (")
                            append(disponibilita)
                            append(")")
                        }
                    }
                },
                color = if (expanded) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Start)
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(VerticalSpacingSmall))

                Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        buildAnnotatedString {
                            append(stringResource(id = R.string.auto))
                            append(": ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = if (animatore.auto) R.string.si else R.string.no))
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                Spacer(modifier = Modifier.height(VerticalSpacingSmall))

                Text(
                    text = animatore.note,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewItemList() {


    val mock = MockDataSource()

    AnimatoreListItem(
        animatore= mock.darioOttobre,
        giornoSelezionato = mock.foglioOttobre.primoGiorno
    )
}