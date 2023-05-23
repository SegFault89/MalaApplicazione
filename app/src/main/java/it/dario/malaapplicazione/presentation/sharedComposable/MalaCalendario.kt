package it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.domain.utils.rangeTo
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.Calendar

private val MY_DAY_OF_WEEK_TO_CALENDAR = arrayOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY)
fun DateFormatSymbols.getShortWeekdaysByMyDayOfWeek() = Array(7) { shortWeekdays[MY_DAY_OF_WEEK_TO_CALENDAR[it]] }

@Preview(showBackground = true)
@Composable
fun MalaCalendario(
    modifier: Modifier = Modifier,
    startDate: LocalDate = LocalDate.of(2023, 8, 1),
    endDate: LocalDate = LocalDate.of(2023, 9, 4),
    dayContent: @Composable (LocalDate) -> Unit = { PreviewDay(day = it)},
    ) {

    val offsetItems = (0 until startDate.dayOfWeek.value-1).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {

        items(DateFormatSymbols().getShortWeekdaysByMyDayOfWeek()) {HeaderDay(it)}
        items(offsetItems) { BlankDay()}
        items(startDate.rangeTo(endDate).toList()) {
            dayContent(it)
        }
    }

}

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
private fun BlankDay() {
    Box(
        modifier = Modifier.aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center,
    ) {
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