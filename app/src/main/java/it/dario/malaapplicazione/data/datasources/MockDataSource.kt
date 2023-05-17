package it.dario.malaapplicazione.data.datasources

import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import it.dario.malaapplicazione.data.model.MalaFile
import it.dario.malaapplicazione.domain.utils.LocalDateIterator
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

/**
 * Implementazione per permettere di testare l'applicazione con dati mockati
 * senza contattattare ancora il foglio di presenze
 */
class MockDataSource : IDisponibilitaDataSource {

    //region dati fittizi
    private val malaFile = MalaFile(fogli = listOf("Ottobre'23", "Novembre'23"))

    private val foglioOttobre = Foglio(
        meseString = "Ottobre",
        annoString = "23",
        meseInt = Month.OCTOBER.value,
        annoInt = 2023,
        primoGiorno = LocalDate.of(2023, Month.OCTOBER.value, 1),
        ultimoGiorno = LocalDate.of(2023, Month.NOVEMBER.value, 1).minusDays(1)
    )

    private val foglioNovembre = Foglio(
        meseString = "Novembre",
        annoString = "23",
        meseInt = Month.NOVEMBER.value,
        annoInt = 2023,
        primoGiorno = LocalDate.of(2023, Month.NOVEMBER.value, 1),
        ultimoGiorno = LocalDate.of(
            2023,
            Month.DECEMBER.value,
            4
        ) //il foglio novembre va fino al 4 dicembre perchè se no è facile
    )

    private val darioOttobre = Animatore(
        nome = "Dario",
        cognome = "Trisconi",
        domicilio = "Seregno",
        auto = false,
        adulti = true,
        bambini = false,
        note = "Datemidabbere"
    )
    private val darioNovembre = Animatore(
        nome = "Dario",
        cognome = "Trisconi",
        domicilio = "Seregno",
        auto = false,
        adulti = true,
        bambini = false,
        note = "Datemidabberedippiù"
    )

    private val silviaOttobre = Animatore(
        nome = "Silvia",
        cognome = "Ratti",
        domicilio = "Seregno",
        auto = true,
        adulti = true,
        bambini = true,
        note = "Mai più ad Ancona"
    )
    private val silviaNovembre = Animatore(
        nome = "Silvia",
        cognome = "Ratti",
        domicilio = "Seregno",
        auto = true,
        adulti = true,
        bambini = true,
        note = "Avevo detto mai più :("
    )


    init {
        foglioOttobre.addAnimatore(silviaOttobre.label, silviaOttobre)
        //foglioOttobre.addAnimatore(darioOttobre.label, darioOttobre)

        foglioNovembre.addAnimatore(silviaNovembre.label, silviaOttobre)
        foglioNovembre.addAnimatore(darioNovembre.label, darioOttobre)

        //Riempimento disponibilità
        LocalDateIterator.iterate(foglioOttobre.primoGiorno, foglioOttobre.ultimoGiorno) {
            silviaOttobre.setDisponibilita(it, "1") //Silvia Sempre disponibile
            darioOttobre.setDisponibilita(
                it,
                if (it.dayOfWeek in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) "1" else "0"
            ) //dario solo i week end
        }

        LocalDateIterator.iterate(foglioNovembre.primoGiorno, foglioNovembre.ultimoGiorno) {
            silviaOttobre.setDisponibilita(it, "1") //Silvia Sempre disponibile
            darioOttobre.setDisponibilita(
                it,
                if (it.dayOfWeek in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) "1" else "0"
            ) //dario solo i week end
        }
    }

    //endregion

    private var foglioSelezionato: Foglio? = null
    override fun getMesi(): List<String> = malaFile.fogli
    override fun getAnimatori(mese: String): List<Animatore> {
        foglioSelezionato = when (mese) {
            foglioOttobre.label -> foglioOttobre
            foglioNovembre.label -> foglioNovembre
            else -> null
        }
        return foglioSelezionato?.animatori?.map { it.value }?.toList() ?: emptyList()
    }

    override fun getAnimatore(animatore: String): Animatore? {
        return foglioSelezionato?.animatori?.get(animatore)
    }

    override fun getFoglio(name: String): Foglio {
        return when (name) {
            foglioOttobre.label -> foglioOttobre
            foglioNovembre.label -> foglioNovembre
            else -> error("come ci sei arrivato qui?")
        }
    }
}