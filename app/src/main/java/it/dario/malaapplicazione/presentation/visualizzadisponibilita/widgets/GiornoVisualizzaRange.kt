package it.dario.malaapplicazione.presentation.visualizzadisponibilita.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilitaViewModel
import java.time.LocalDate

@Composable
fun GiornoVisualizzaRange(
    viewModel: VisualizzaDisponibilitaViewModel,
    day: LocalDate,
) {

    val firstDaySelected by viewModel.primoGiornoSelezionato.collectAsState()
    val lastDaySelected by viewModel.ultimoGiornoSelezionato.collectAsState()

    val isBetween = (firstDaySelected != null && lastDaySelected != null) &&
            (firstDaySelected!!.isBefore(day) && lastDaySelected!!.isAfter(day))

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(if (day == firstDaySelected || day == lastDaySelected) 2.dp else if (isBetween) 6.dp else 0.dp ),
        contentAlignment = Alignment.Center,
    ) {
        OutlinedButton(onClick = { viewModel.selectGiornoForRange(day) },
            modifier= Modifier.fillMaxSize(),  //avoid the oval shape
            shape = CircleShape,
            border= if (day == firstDaySelected || day == lastDaySelected || isBetween) {
                BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
            } else {
                null
            },
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = Color.Transparent
            )
        ) {
            Text(
                text = day.dayOfMonth.toString(),
            )
        }

    }
}
