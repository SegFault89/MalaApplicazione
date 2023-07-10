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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.model.DisponibilitaGiornaliere
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.theme.DisponibileGreen
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
    Card(
        onClick = { viewModel.updateSelectedDay(day) },
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = if (daySelected == day) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        )
    ) {
        Text(
            modifier = Modifier
                .weight(.5f)
                .fillMaxWidth(),
            text = day.dayOfMonth.toString(),
            textAlign = TextAlign.Center,
        )

        Row(
            modifier = Modifier
                .weight(.5f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NumeriDisponibilita(disponibilitaGiornaliere)
        }
    }
}


@Composable
fun NumeriDisponibilita(disponibilitaGiornaliere: DisponibilitaGiornaliere) {
    if (disponibilitaGiornaliere.disponibili > 0) {
        Box(
            modifier = Modifier
                .padding(1.dp)
                .background(DisponibileGreen, shape = CircleShape)
                .aspectRatio(1f)
                .wrapContentSize()
        )
        {
            Text(
                text = disponibilitaGiornaliere.disponibili.toString(),
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                color = TextOverColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    if (disponibilitaGiornaliere.altro > 0) {
        Box(
            modifier = Modifier
                .padding(1.dp)
                .background(DisponibileYellow, shape = CircleShape)
                .aspectRatio(1f)
                .wrapContentSize()
        ) {
            Text(
                text = disponibilitaGiornaliere.altro.toString(),
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                color = TextOverColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
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
            updateFoglioSelezionato(mock.foglioOttobre.label)
        },
        day = mock.foglioOttobre.primoGiorno,
        foglio = mock.foglioOttobre.label,
    )
}
