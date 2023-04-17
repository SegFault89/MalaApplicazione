package it.dario.malaapplicazione.presentation.inserisciDisponibilita.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth

@Preview
@Composable
fun CalendarInserisci(
    modifier: Modifier = Modifier,
    startDate: LocalDate = LocalDate.of(2023, 8, 1),
    endDate: LocalDate = LocalDate.of(2023, 8, 31)
) {
    //https://github.com/kizitonwose/Calendar/blob/main/docs/Compose.md

    val currentMonth = remember { YearMonth.of(startDate.year, startDate.month) }
    val startMonth = remember { YearMonth.of(startDate.year, startDate.month) }
    val endMonth = remember { YearMonth.of(endDate.year, endDate.month) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        state = state,
        dayContent = { Day(it) }
    )
}


@Composable
fun Day(day: CalendarDay) {
    Box(
        modifier = Modifier
            .aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.date.dayOfMonth.toString())
    }
}

/*
@Composable
fun DatePicker(
    state: DatePickerState,
    modifier: Modifier = Modifier,
    dateFormatter: DatePickerFormatter = remember { DatePickerFormatter() },
    dateValidator: (Long) -> Boolean = { true },
    title: (@Composable () -> Unit)? = {
        DatePickerDefaults.DatePickerTitle(
            state,
            modifier = Modifier.padding(DatePickerTitlePadding)
        )
    },
    headline: (@Composable () -> Unit)? = {
        DatePickerDefaults.DatePickerHeadline(
            state,
            dateFormatter,
            modifier = Modifier.padding(DatePickerHeadlinePadding)
        )
    },
    showModeToggle: Boolean = true,
    colors: DatePickerColors = DatePickerDefaults.colors()
): Unit

 */