package it.dario.malaapplicazione.presentation.visualizzadisponibilita.widgets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.model.DisponibilitaGiornaliere
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.theme.DisponibileGreen
import it.dario.malaapplicazione.presentation.theme.DisponibileRed
import it.dario.malaapplicazione.presentation.theme.DisponibileYellow
import it.dario.malaapplicazione.presentation.theme.TextOverColor
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilitaViewModel
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiornoVisualizza(
    viewModel: VisualizzaDisponibilitaViewModel,
    day: LocalDate,
    foglio: String,
) {

    Log.d(Constants.TAG, "composing GiornoVisualizza for $day")
    val daySelected by viewModel.giornoSelezionato.collectAsState()

    val disponibilitaGiornaliere = viewModel.getDisponibilitaGiornaliere(foglio, day)
    Box(
        modifier = Modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Card(
            onClick = { viewModel.updateSelectedDay(day) },
            modifier = Modifier
                .padding(2.dp)
                .fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = if (daySelected == day) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                //contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.dayOfMonth.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NumeriDisponibilita(disponibilitaGiornaliere)
                }
            }
        }
    }
}


@Composable
fun NumeriDisponibilita(disponibilitaGiornaliere: DisponibilitaGiornaliere) {
    if (disponibilitaGiornaliere.disponibili > 0) {
        Text(
            text = disponibilitaGiornaliere.disponibili.toString(),
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            color = TextOverColor,
            modifier = Modifier
                .background(DisponibileGreen, shape = CircleShape)
                .padding(1.dp),
        )
    }
    if (disponibilitaGiornaliere.altro > 0) {
        Text(
            text = disponibilitaGiornaliere.altro.toString(),
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            color = TextOverColor,
            modifier = Modifier
                .background(DisponibileYellow, shape = CircleShape)
                .padding(1.dp),
        )
    }
    if (disponibilitaGiornaliere.nonDisponibili > 0) {
        Text(
            text = disponibilitaGiornaliere.nonDisponibili.toString(),
            textAlign = TextAlign.Center,
            color = TextOverColor,
            fontSize = 10.sp,
            modifier = Modifier
                .background(DisponibileRed, shape = CircleShape)
                .padding(1.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGiornoVisualizza() {
    val mock = MockDataSource()
    GiornoVisualizza(
        viewModel = VisualizzaDisponibilitaViewModel(
            DisponibilitaRepository(
                mock
            )
        ).apply {
            updateFoglioSelezionato(mock.foglioNovembre.label)
        },
        day = mock.foglioNovembre.primoGiorno,
        foglio = mock.foglioNovembre.label,
    )
}