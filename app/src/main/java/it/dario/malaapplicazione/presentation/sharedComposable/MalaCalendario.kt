package it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.MalaViewModel
import java.time.LocalDate

@Preview
@Composable
fun MalaCalendario(
    modifier: Modifier = Modifier,
    viewModel: MalaViewModel = MalaViewModel(DisponibilitaRepository(MockDataSource())),
    startDate: LocalDate = LocalDate.of(2023, 8, 1),
    endDate: LocalDate = LocalDate.of(2023, 8, 31),
    dayContent: @Composable BoxScope.(LocalDate) -> Unit = { PreviewDay(day = it)},
    ) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {

        items(headers) {HeaderDay(it)}
        items((1..31).toList()) {
            PreviewDay(LocalDate.of(2023,5,it))
        }
    }

}

private val headers = listOf("Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom")

@Composable
private fun HeaderDay(label: String) {
    Box(
        modifier = Modifier.aspectRatio(2f), // This is important for square sizing!
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
        )

    }
}



@Composable
private fun PreviewDay(day: LocalDate) {
    Box(
        modifier = Modifier.aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center,
    ) {
            Text(
                text = day.dayOfMonth.toString(),
            )

    }
}