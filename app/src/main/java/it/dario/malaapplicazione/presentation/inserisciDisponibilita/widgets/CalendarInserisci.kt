package it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.asLiveData
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import it.dario.malaapplicazione.data.Constants.DISPONIBILE
import it.dario.malaapplicazione.data.Constants.NON_DISPONIBILE
import it.dario.malaapplicazione.data.Constants.NO_DISPONIBILITA
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.MalaViewModel
import java.time.LocalDate
import java.time.YearMonth

@Preview
@Composable
fun CalendarInserisci(
    modifier: Modifier = Modifier,
    viewModel: MalaViewModel = MalaViewModel(DisponibilitaRepository(MockDataSource())),
    startDate: LocalDate = LocalDate.of(2023, 8, 1),
    endDate: LocalDate = LocalDate.of(2023, 8, 31)
) {
    //https://github.com/kizitonwose/Calendar/blob/main/docs/Compose.md

    val currentMonth = remember { YearMonth.of(startDate.year, startDate.month) }
    val startMonth = remember { YearMonth.of(startDate.year, startDate.month) }
    val endMonth = remember { YearMonth.of(endDate.year, endDate.month) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val animatore = viewModel.disponibilitàAnimatore.asLiveData()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        state = state,
        dayContent = {
            Day(
                day = it,
                enabled = !(it.date.isBefore(startDate) || it.date.isAfter(endDate)),
                value = animatore.value!!.getDisponibilita(it.date)
            )
        }
    )
}


@Composable
fun Day(day: CalendarDay, enabled: Boolean, value: String) {
    Box(
        modifier = Modifier.aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            color = when (value) {
                NON_DISPONIBILE -> Color.Red
                DISPONIBILE -> Color.Green
                NO_DISPONIBILITA -> Color.Transparent
                else -> Color.Yellow
            }
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = if (enabled) Color.Black else Color.LightGray,
            )
        }

    }
}
