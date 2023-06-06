package it.dario.malaapplicazione.data.datasources

import android.content.Context
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import it.dario.malaapplicazione.data.model.MalaFile
import it.dario.malaapplicazione.domain.utils.LocalDateIterator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

/**
 * Implementazione per permettere di testare l'applicazione con dati mockati
 * senza contattattare ancora il foglio di presenze
 */
class MockDataSource : IDisponibilitaDataSource {


    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean>
        get() = _isReady


    //region dati fittizi
    val labelOttobre = "Ottobre '23"
    val labelNovembre = "Novembre '23"
    val malaFile = MalaFile(fogli = listOf(labelOttobre, labelNovembre))

    val foglioOttobre = Foglio(
        label = labelOttobre,
        primoGiorno = LocalDate.of(2023, Month.OCTOBER.value, 1),
        ultimoGiorno = LocalDate.of(2023, Month.NOVEMBER.value, 1).minusDays(1)
    )

    val foglioNovembre = Foglio(
        label = labelNovembre,
        primoGiorno = LocalDate.of(2023, Month.NOVEMBER.value, 1),
        ultimoGiorno = LocalDate.of(
            2023,
            Month.DECEMBER.value,
            4
        ) //il foglio novembre va fino al 4 dicembre perchè se no è facile
    )

    val darioOttobre = Animatore(
        index = 1,
        nome = "Dario",
        cognome = "Trisconi",
        _domicilio = "Seregno",
        _auto = false,
        _adulti = true,
        _bambini = false,
        _note = "Datemidabbere"
    )
    val darioNovembre = Animatore(
        index = 1,
        nome = "Dario",
        cognome = "Trisconi",
        _domicilio = "Seregno",
        _auto = false,
        _adulti = true,
        _bambini = false,
        _note = "Datemidabberedippiù"
    )

    val silviaOttobre = Animatore(
        index = 2,
        nome = "Silvia",
        cognome = "Ratti",
        _domicilio = "Seregno",
        _auto = true,
        _adulti = true,
        _bambini = true,
        _note = "Mai più ad Ancona"
    )
    val silviaNovembre = Animatore(
        index = 2,
        nome = "Silvia",
        cognome = "Ratti",
        _domicilio = "Seregno",
        _auto = true,
        _adulti = true,
        _bambini = true,
        _note = "Avevo detto mai più :("
    )


    override suspend fun setup(context: Context) {
        foglioOttobre.addAnimatore(silviaOttobre.label, silviaOttobre)
        foglioOttobre.addAnimatore(darioOttobre.label, darioOttobre)

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

        _isReady.value = true
    }

    //endregion

    //private var foglioSelezionato: Foglio? = null
    override fun getFogli(): List<String> = malaFile.fogli

    override suspend fun fetchAnimatoriInFoglio(foglio: String, complete: Boolean): List<Animatore> =
        getFoglio(foglio).animatori.map { it.value }.toList()


    override fun getAnimatore(foglio: String, animatore: String): Animatore {
        return getFoglio(foglio).animatori[animatore]!!
    }

    override fun getDomicilioAsFlow(foglio: String, animatore: String): StateFlow<String> {
        return getAnimatore(foglio, animatore).getDomicilioAsFlow()
    }

    override fun getAutoAsFlow(foglio: String, animatore: String): StateFlow<Boolean> {
        return getAnimatore(foglio, animatore).getAutoAsFlow()
    }

    override fun getBambiniAsFlow(foglio: String, animatore: String): StateFlow<Boolean> {
        return getAnimatore(foglio, animatore).getBambiniAsFlow()
    }

    override fun getAdultiAsFlow(foglio: String, animatore: String): StateFlow<Boolean> {
        return getAnimatore(foglio, animatore).getAdultiAsFlow()
    }

    override fun getNoteAsFlow(foglio: String, animatore: String): StateFlow<String> {
        return getAnimatore(foglio, animatore).getNoteAsFlow()
    }

    override suspend fun updateDomicilio(foglio: String, animatore: String, value: String) {
        getAnimatore(foglio, animatore).updateDomicilio(value)
    }

    override suspend fun updateAuto(foglio: String, animatore: String, value: Boolean) {
        getAnimatore(foglio, animatore).updateAuto(value)
    }

    override suspend fun updateBambini(foglio: String, animatore: String, value: Boolean) {
        getAnimatore(foglio, animatore).updateBambini(value)
    }

    override suspend fun updateAdulti(foglio: String, animatore: String, value: Boolean) {
        getAnimatore(foglio, animatore).updateAdulti(value)
    }

    override suspend fun updateNote(foglio: String, animatore: String, value: String) {
        getAnimatore(foglio, animatore).updateNote(value)
    }

    override suspend fun refreshAnimatore(foglio: String, animatore: String) {
        /*nothimg, nel mock non c'è bisogno di refresshare*/
    }

    override fun getFoglio(name: String): Foglio {
        return when (name) {
            foglioOttobre.label -> foglioOttobre
            foglioNovembre.label -> foglioNovembre
            else -> error("come ci sei arrivato qui?")
        }
    }

    override fun getDisponibilitaAsFlow(foglio: String, animatore: String, date: LocalDate): StateFlow<String> =
        getAnimatore(foglio, animatore)!!.getDisponibilitaAsFlow(date)

    override suspend fun updateDisponibilita(
        foglio: String,
        animatore: String,
        date: LocalDate,
        content: String
    ) {
        getAnimatore(foglio, animatore).updateDisponibilita(date, content)
    }
}
