package it.dario.malaapplicazione.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


data class DisponibilitaGiornaliere (
    val disponibili: Int,
    val nonDisponibili: Int,
    val altro: Int
)