package it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.data.Constants
import it.dario.malaapplicazione.domain.utils.rangeTo
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.Calendar

private val MY_DAY_OF_WEEK_TO_CALENDAR = arrayOf(
    Calendar.MONDAY,
    Calendar.TUESDAY,
    Calendar.WEDNESDAY,
    Calendar.THURSDAY,
    Calendar.FRIDAY,
    Calendar.SATURDAY,
    Calendar.SUNDAY
)

fun DateFormatSymbols.getShortWeekdaysByMyDayOfWeek() =
    Array(7) { shortWeekdays[MY_DAY_OF_WEEK_TO_CALENDAR[it]] }

@Preview(showBackground = true)
@Composable
fun MalaCalendario(
    modifier: Modifier = Modifier,
    startDate: LocalDate = LocalDate.of(2023, 8, 1),
    endDate: LocalDate = LocalDate.of(2023, 9, 4),
    dayContent: @Composable (LocalDate) -> Unit = { PreviewDay(day = it) },
) {

    Log.d(Constants.TAG, "composing MalaCalendario")

    val offsetItems = (0 until startDate.dayOfWeek.value - 1).toList()
    val columns = 7
    val days = startDate.rangeTo(endDate).toList()

    //non includo gli headers visto che hanno già la loro riga
    val itemCount = offsetItems.count() + //+ offset
            days.count() // + numero giorni
    val rows = (itemCount / columns)

    var itemInserted = 0

    Column(modifier = modifier) {

        HeaderRow(modifier = modifier) //headers

        /* prima riga (con offset)*/
        Row {
            offsetItems.forEach {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    BlankDay()
                }
            }
            days.take(columns - offsetItems.count()).forEach {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    itemInserted++
                    dayContent(it)
                }
            }
        }

        //iteminserted = 6
        /* gestione di tutte le altre righe*/
        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (itemInserted < days.count()) {
                            dayContent(days[itemInserted++])
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderRow(
    modifier: Modifier
) {
    Row(modifier = modifier) {
        DateFormatSymbols().getShortWeekdaysByMyDayOfWeek().forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { HeaderDay(label = it) }
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


@Composable
fun NonlazyGrid(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable() (Int) -> Unit
) {
    Column(modifier = modifier) {
        var rows = (itemCount / columns)
        if (itemCount.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
                            content(index)
                        }
                    }
                }
            }
        }
    }
}