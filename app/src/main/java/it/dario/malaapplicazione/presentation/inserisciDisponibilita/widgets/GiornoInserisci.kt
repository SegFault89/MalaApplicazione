package it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import it.dario.malaapplicazione.data.Constants
import java.time.LocalDate


@Composable
fun GiornoInserisci(
    day: LocalDate,
    value: String
) {
    Box(
        modifier = Modifier.aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            color = when (value) {
                Constants.NON_DISPONIBILE -> Color.Red
                Constants.DISPONIBILE -> Color.Green
                Constants.NO_DISPONIBILITA -> Color.Transparent
                else -> Color.Yellow
            }
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                //color = if (enabled) Color.Black else Color.LightGray,
            )
        }

    }
}