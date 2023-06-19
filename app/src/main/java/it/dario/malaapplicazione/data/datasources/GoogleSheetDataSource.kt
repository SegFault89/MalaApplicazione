package it.dario.malaapplicazione.data.datasources

import android.content.Context
import android.util.Log
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.Constants.NO_DISPONIBILITA
import it.dario.malaapplicazione.data.Constants.TAG
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.BASE_YEAR
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.COLONNE_NOME_COGNOME_ANIMATORI
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.FILENAME_REGEX
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.FILE_NAME_SEPARATOR
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_ADULTI
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_ADULTI_INT
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_AUTO
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_AUTO_INT
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_BAMBINI
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_BAMBINI_INT
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_COGNOME_INT
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_NOME_INT
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_PRIMO_GIORNO_INT
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_RESIDENZA
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_RESIDENZA_INT
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_RIGA_PRIMO_ANIMATORE
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.RAW
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.RIGA_GIORNI
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.getRigheAnimatoriCoompleti
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.mapMonth
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import it.dario.malaapplicazione.data.model.MalaFile
import it.dario.malaapplicazione.data.secret.Secret.GOOLE_SPREADSHEET
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


object GoogleSheetDataSource : IDisponibilitaDataSource {


    private val APPLICATION_NAME = "MalaApplicazione" //TODO mettere in buildConfig?
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    private val SCOPES = listOf(SheetsScopes.SPREADSHEETS)

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean>
        get() = _isReady

    /**
     * setuppa un handler per permettere di eseguire azioni a fronte di un errore (esempio: mostrare un messaggio)
     */
    private var errorHandler: IDatasourceErrorHandler? = null


    private lateinit var service: Sheets

    private lateinit var malaFile: MalaFile

    override suspend fun setup(context: Context) {

        val `in`: InputStream = context.resources.openRawResource(R.raw.credential_secret)
        // Build flow and trigger user authorization request.
        val credentials = GoogleCredentials.fromStream(`in`).createScoped(SCOPES)

        val _httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        service = Sheets.Builder(_httpTransport, JSON_FACTORY, HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME)
            .build()

        malaFile = MalaFile(fogli = getSheets())

        _isReady.value = true
    }

    override fun setErrorHandler(e: IDatasourceErrorHandler) {
        errorHandler = e
    }

    private fun getSheets(): List<String> =
        try {
            service.spreadsheets()[GOOLE_SPREADSHEET].apply { includeGridData = false }
                .execute().sheets.map { it.properties.title }.filter { FILENAME_REGEX.matches(it) }
                .toList()
        } catch (t: Throwable) {
            errorHandler?.onGetFogliError(t)
            listOf()
        }

    override fun getFogli(): List<String> = malaFile.fogli

    private fun getRangeNomiCognomiAnimatori(foglio: String): ValueRange = try {
        service.spreadsheets()
            .values()[GOOLE_SPREADSHEET, "$foglio!$COLONNE_NOME_COGNOME_ANIMATORI"].execute()
    } catch (t: Throwable) {
        errorHandler?.onGetAnimatoriError(t)
        ValueRange()
    }

    private fun fetchAnimatori(foglio: String): List<Animatore> {
        val result = mutableListOf<Animatore>()
        val response: ValueRange = getRangeNomiCognomiAnimatori(foglio)
        Log.d(TAG, "animatori trovati per $foglio -> ${response.getValues().size}")
        response.getValues().forEachIndexed { i, v ->
            try {
                result.add(
                    Animatore(
                        index = i,
                        nome = v[0].toString(),
                        cognome = v[1].toString(),
                    )
                )
            } catch (t: Throwable) {
                Log.d(TAG, "Sembra non esserci nome o cognome per la riga $i nel foglio $foglio")
            }
        }
        return result
    }

    private fun getRangeNomiCognomiAnimatoriComplete(foglio: String, noteIndex: Int): ValueRange =
        try {
            service.spreadsheets()
                .values()[GOOLE_SPREADSHEET, "$foglio!${getRigheAnimatoriCoompleti(indiceNote = noteIndex)}"].execute()
        } catch (t: Throwable) {
            errorHandler?.onGetAnimatoriCompleteError(t)
            ValueRange()
        }

    private fun fetchAnimatoriComplete(foglio: String, sheet: Foglio): List<Animatore> {
        val result = mutableListOf<Animatore>()
        val indiceNote = INDICE_COLONNA_PRIMO_GIORNO_INT + sheet.dayNum.toInt()

        val response: ValueRange = getRangeNomiCognomiAnimatoriComplete(foglio, indiceNote)
        Log.d(TAG, "animatori trovati per $foglio -> ${response.getValues().size}")
        response.getValues().forEachIndexed { i, v ->
            lateinit var anim: Animatore
            try {
                anim = Animatore(
                    index = i,
                    nome = v[INDICE_COLONNA_NOME_INT - 1].toString(),
                    cognome = v[INDICE_COLONNA_COGNOME_INT - 1].toString(),
                    _domicilio = v[INDICE_COLONNA_RESIDENZA_INT - 1]?.toString() ?: "",
                    _auto = v[INDICE_COLONNA_AUTO_INT - 1]?.let { it.toString() == "1" } ?: false,
                    _adulti = v[INDICE_COLONNA_ADULTI_INT - 1]?.let { it.toString() == "1" }
                        ?: false,
                    _bambini = v[INDICE_COLONNA_BAMBINI_INT - 1]?.let { it.toString() == "1" }
                        ?: false,
                    _note = if (v.size == indiceNote - 1) {
                        ""
                    } else {
                        v[indiceNote - 1].toString()
                    }
                )
            } catch (t: Throwable) {
                Log.d(TAG, "Sembra non esserci nome o cognome per la riga $i nel foglio $foglio")
                return@forEachIndexed
            }
            v.forEachIndexed { index, value ->
                if (index < INDICE_COLONNA_PRIMO_GIORNO_INT - 1) {
                    /* nothing */
                } else {
                    anim.setDisponibilita(
                        sheet.primoGiorno.plusDays((index + 1 - INDICE_COLONNA_PRIMO_GIORNO_INT).toLong()),
                        value?.toString() ?: NO_DISPONIBILITA
                    )
                }
            }
            result.add(anim)
        }
        return result
    }

    private fun getGiorni(foglio: String) = try {
        service.spreadsheets().values()[GOOLE_SPREADSHEET, "$foglio!$RIGA_GIORNI"].execute()
    } catch (t: Throwable) {
        errorHandler?.onGetGiorniError(t)
        ValueRange()
    }

    fun fetchFoglio(name: String, complete: Boolean = false): Foglio {

        val giorniNelFoglio = getGiorni(name)

        Log.d(TAG, "Numero giorni = ${(giorniNelFoglio.getValues().first().size)}")

        val splitted = name.split(FILE_NAME_SEPARATOR)

        val first = giorniNelFoglio.getValues().first().first().toString().split(" ")
        val last = giorniNelFoglio.getValues().first().last().toString().split(" ")

        val primoGiorno =
            LocalDate.of(
                BASE_YEAR + splitted[1].toInt(),
                mapMonth[first[1].lowercase().trim()]!!,
                first[0].toInt()
            )
        val ultimoGiorno =
            LocalDate.of(
                BASE_YEAR + splitted[1].toInt(),
                mapMonth[last[1].lowercase().trim()]!!,
                last[0].toInt()
            )

        val result = Foglio(
            label = name,
            primoGiorno = primoGiorno,
            ultimoGiorno = ultimoGiorno,
            dataAggiornamento = LocalDateTime.now()
        )
        if (complete) {
            fetchAnimatoriComplete(name, result)
        } else {
            fetchAnimatori(name)
        }.forEach { result.addAnimatore(it.label, it) }
        return result
    }

    override suspend fun fetchAnimatoriInFoglio(
        foglio: String,
        complete: Boolean,
        force: Boolean
    ): List<Animatore> {
        //controllo se il foglio esiste già nel malaFIle, altrimento lo scarico e lo aggiungo al file
        val mFoglio =
            malaFile.malaFogli[foglio] ?: fetchFoglio(
                foglio,
                complete
            ).also { malaFile.addFoglio(it) }

        if (force || (complete && LocalDateTime.now().minusMinutes(10).isBefore(mFoglio.dataAggiornamento))) {
            malaFile.updateFoglio(fetchFoglio(foglio, true))
        }
        return mFoglio.getAnimatoriAsList()

    }

    private fun getAnimatoreCompleto(foglio: String, rowIndex: Int) = try {
        service.spreadsheets()
            .values()[GOOLE_SPREADSHEET, "$foglio!$INDICE_COLONNA_RESIDENZA$rowIndex:$rowIndex"].execute()
            .getValues().first()
    } catch (t: Throwable) {
        errorHandler?.onGetAnimatoreError(t)
        listOf()
    }

    override suspend fun refreshAnimatore(foglio: String, animatore: String) {
        //TODO aggiungere controlli per presenza del file e dell'animatore
        val sheet = malaFile.malaFogli[foglio]
        val toUpdate = sheet!!.animatori[animatore]!!

        Log.d(TAG, "refresh animatore $animatore in foglio $foglio")
        if (LocalDateTime.now().minusMinutes(5).isBefore(toUpdate.dataAggiornamento)) {
            Log.d(TAG, "dati abbastanza freschi, salto il refresh")
            return
        }
        val rowIndex = INDICE_RIGA_PRIMO_ANIMATORE + toUpdate.index
        val noteIndex = (sheet.dayNum + 4).toInt()

        val line = getAnimatoreCompleto(foglio, rowIndex)

        line.forEachIndexed { i, v ->
            when (i) {
                0 -> toUpdate.updateDomicilio(v.toString())
                1 -> toUpdate.updateAuto(v.toString() == "1")
                2 -> toUpdate.updateAdulti(v.toString() == "1")
                3 -> toUpdate.updateBambini(v.toString() == "1")
                noteIndex -> toUpdate.updateNote(v.toString())
                else -> toUpdate.setDisponibilita(
                    sheet.primoGiorno.plusDays(i - 4L),
                    v.toString()
                )
            }
        }
        toUpdate.dataAggiornamento = LocalDateTime.now()
    }


//endregion

//private var foglioSelezionato: Foglio? = null

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

    private fun updateGoogleCell(foglio: String, cella: String, value: String) {
        val x = ValueRange().setValues(listOf(listOf(value)))
        try {
            service.spreadsheets().values().update(GOOLE_SPREADSHEET, "$foglio!$cella", x)
                .setValueInputOption(RAW)
                .execute()
        } catch (t: Throwable) {
            errorHandler?.onUpdateCellError(t)
        }
    }

    override suspend fun updateDomicilio(foglio: String, animatore: String, value: String) {
        val anim = getAnimatore(foglio, animatore)
        CoroutineScope(IO).launch {
            updateGoogleCell(
                foglio,
                "$INDICE_COLONNA_RESIDENZA${INDICE_RIGA_PRIMO_ANIMATORE + anim.index}",
                value
            )
        }
        anim.updateDomicilio(value)
    }

    override suspend fun updateAuto(foglio: String, animatore: String, value: Boolean) {
        val anim = getAnimatore(foglio, animatore)
        updateGoogleCell(
            foglio,
            "$INDICE_COLONNA_AUTO${INDICE_RIGA_PRIMO_ANIMATORE + anim.index}",
            if (value) "1" else "0"
        )
        anim.updateAuto(value)
    }

    override suspend fun updateBambini(foglio: String, animatore: String, value: Boolean) {
        val anim = getAnimatore(foglio, animatore)
        updateGoogleCell(
            foglio,
            "$INDICE_COLONNA_BAMBINI${INDICE_RIGA_PRIMO_ANIMATORE + anim.index}",
            if (value) "1" else "0"
        )
        anim.updateBambini(value)
    }

    override suspend fun updateAdulti(foglio: String, animatore: String, value: Boolean) {
        val anim = getAnimatore(foglio, animatore)
        updateGoogleCell(
            foglio,
            "$INDICE_COLONNA_ADULTI${INDICE_RIGA_PRIMO_ANIMATORE + anim.index}",
            if (value) "1" else "0"
        )
        anim.updateAdulti(value)
    }

    override suspend fun updateNote(foglio: String, animatore: String, value: String) {
        val sheet = malaFile.malaFogli[foglio]!!
        val anim = getAnimatore(foglio, animatore)
        CoroutineScope(IO).launch {
            val indiceNote = INDICE_COLONNA_PRIMO_GIORNO_INT + sheet.dayNum
            updateGoogleCell(
                foglio,
                "R${INDICE_RIGA_PRIMO_ANIMATORE + anim.index}C$indiceNote",
                value
            )
        }
        anim.updateNote(value)
    }

    override fun getFoglio(name: String): Foglio {
        return malaFile.malaFogli[name] ?: error("Non è stato possibile trovare il foglio $name")
    }

    override fun getDisponibilitaAsFlow(
        foglio: String,
        animatore: String,
        date: LocalDate
    ): StateFlow<String> =
        getAnimatore(foglio, animatore).getDisponibilitaAsFlow(date)

    override suspend fun updateDisponibilita(
        foglio: String,
        animatore: String,
        date: LocalDate,
        content: String
    ) {
        val sheet = malaFile.malaFogli[foglio]!!
        val anim = getAnimatore(foglio, animatore)
        CoroutineScope(IO).launch {
            val indice = INDICE_COLONNA_PRIMO_GIORNO_INT +
                    ChronoUnit.DAYS.between(sheet.primoGiorno, date)
            updateGoogleCell(
                foglio,
                //"R${INDICE_RIGA_PRIMO_ANIMATORE + anim.index}C$indice",
                "R${INDICE_RIGA_PRIMO_ANIMATORE + anim.index}C$indice:R${INDICE_RIGA_PRIMO_ANIMATORE + anim.index}C$indice",
                content
            )
        }
        anim.updateDisponibilita(date, content)
    }
}
