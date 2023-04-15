package it.dario.malaapplicazione.domain.utils

import java.time.LocalDate

/**
 * classi per permettere di iterare giorno per giorno tra due date
 *
 * esempio di utilizzo:  LocalDateIterator.iterate(startDate, endDate) { ... }
 */
object LocalDateIterator {

    fun iterate (start: LocalDate, end: LocalDate, func: (LocalDate) -> Unit ) {
        for (date in start..end) func(date)
    }
}


class DateIterator (start: LocalDate,
                         val end: LocalDate): Iterator<LocalDate> {
    private var currentDate = start

    override fun hasNext() = currentDate <= end

    override fun next(): LocalDate {

        val next = currentDate

        currentDate = currentDate.plusDays(1)

        return next

    }

}

class LocalDateProgression(override val start: LocalDate,
                           override val endInclusive: LocalDate,
) :
    Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> =
        DateIterator(start, endInclusive)

}

operator fun LocalDate.rangeTo(other: LocalDate) = LocalDateProgression(this, other)

