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
import it.dario.malaapplicazione.data.Constants.TAG
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.BASE_YEAR
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.COLONNE_NOME_COGNOME_ANIMATORI
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.FILENAME_REGEX
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.FILE_NAME_SEPARATOR
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_ADULTI
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_AUTO
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_BAMBINI
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_PRIMO_GIORNO_INT
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_COLONNA_RESIDENZA
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.INDICE_RIGA_PRIMO_ANIMATORE
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.RAW
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.RIGA_GIORNI
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

    private fun getSheets(): List<String> =
        service.spreadsheets()[GOOLE_SPREADSHEET].apply { includeGridData = false }
            .execute().sheets.map { it.properties.title }.filter { FILENAME_REGEX.matches(it) }
            .toList()

    override fun getFogli(): List<String> = malaFile.fogli


    private suspend fun fetchAnimatori(foglio: String): List<Animatore> {
        val result = mutableListOf<Animatore>()
        val response: ValueRange = service.spreadsheets()
            .values()[GOOLE_SPREADSHEET, "$foglio!$COLONNE_NOME_COGNOME_ANIMATORI"].execute()
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
                Log.d(TAG, "Sembra non esserci nome o cognome per la riga $i frl foglio $foglio")
            }
        }
        return result
    }

    private suspend fun fetchFoglio(name: String): Foglio {

        val giorniNelFoglio =
            service.spreadsheets().values()[GOOLE_SPREADSHEET, "$name!$RIGA_GIORNI"].execute()
        Log.d(TAG, "Numero giorni = ${(giorniNelFoglio.getValues().first().size)}")

        val splitted = name.split(FILE_NAME_SEPARATOR)

        val first = giorniNelFoglio.getValues().first().first().toString().split(" ")
        val last = giorniNelFoglio.getValues().first().last().toString().split(" ")

        val primoGiorno =
            LocalDate.of(
                BASE_YEAR + splitted[1].toInt(),
                mapMonth[first[1].lowercase()]!!,
                first[0].toInt()
            )
        val ultimoGiorno =
            LocalDate.of(
                BASE_YEAR + splitted[1].toInt(),
                mapMonth[last[1].lowercase()]!!,
                last[0].toInt()
            )

        val result = Foglio(
            label = name,
            primoGiorno = primoGiorno,
            ultimoGiorno = ultimoGiorno
        )
        fetchAnimatori(name).forEach { result.addAnimatore(it.label, it) }

        return result
    }

    override suspend fun getAnimatori(foglio: String): List<Animatore> {
        //controllo se il foglio esiste già nel malaFIle, altrimento lo scarico e lo aggiungo al file
        val mFoglio =
            malaFile.malaFogli[foglio] ?: fetchFoglio(foglio).also { malaFile.addFoglio(it) }
        //TODO controllo quanto è passato dal download del foglio per refressharlo?
        //restituisco la lista degli animatori
        return mFoglio.getAnimatoriAsList()

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

        val line = service.spreadsheets()
            .values()[GOOLE_SPREADSHEET, "$foglio!$INDICE_COLONNA_RESIDENZA$rowIndex:$rowIndex"].execute()
            .getValues().first()

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
    /*
        @Throws(IOException::class)
        private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): GoogleCredentials? {
                    // Load client secrets.
            val `in`: InputStream = context.resources.openRawResource(R.raw.credential_secret)
            // Build flow and trigger user authorization request.

            return GoogleCredentials.fromStream(`in`).createScoped(SCOPES)
        }

        @Throws(IOException::class, GeneralSecurityException::class)
        suspend fun foobar() {
            // Build a new authorized API client service.
            val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
            val spreadsheetId = Secret.GOOLE_SPREADSHEET
            val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpCredentialsAdapter(getCredentials(HTTP_TRANSPORT)))
                .setApplicationName(APPLICATION_NAME)
                .build()


            service.spreadsheets().get(spreadsheetId).apply {  includeGridData = false }.execute().sheets.forEach {
                Log.d(TAG, "NAME: ${it.properties.title}")
            }




            val range = "Novembre '22!A2:E10"
            val response: ValueRange = service.spreadsheets().values()[spreadsheetId, range]
                .execute()
            val values: List<List<Any>> = response.getValues()
            if (values == null || values.isEmpty()) {
                Log.d(TAG, "no data found")
            } else {
                for (row in values) {
                    // Print columns A and E, which correspond to indices 0 and 4.
                    Log.d(TAG, "${row[0]}, ${row[4]}")
                }
            }

            //WRITE
            val x = ValueRange()
            x.setValues(listOf(listOf("YATTA")))

            service.spreadsheets().values().update(spreadsheetId, "Novembre '22!A1", x)
                .setValueInputOption("RAW")
                .execute()

            //COLUMN SIZE
            val columns = service.spreadsheets().values()[spreadsheetId, "Novembre '22!B4:B"].execute()
            Log.d(TAG, "ROW NUM = ${columns.getValues().size}")
            columns.getValues().forEachIndexed { i, v ->
                try {
                    Log.d(TAG, "row $i = ${v[0]}")
                } catch (t: Throwable) {
                    Log.d(TAG, "row $i empty")
                }
            }



            //ROW SIZE
            val rows = service.spreadsheets().values()[spreadsheetId, "Novembre '22!G2:2"].execute()
            Log.d(TAG, "ROW LENGTH = ${(rows.getValues().first().size)}")
            rows.getValues().first().forEachIndexed { i, v ->
                Log.d(TAG, "column $i = $v")
            }


        }
    */


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
        //TODO gestione errori
        service.spreadsheets().values().update(GOOLE_SPREADSHEET, "$foglio!$cella", x)
            .setValueInputOption(RAW)
            .execute()
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
        CoroutineScope(IO). launch {
            val indice = INDICE_COLONNA_PRIMO_GIORNO_INT +
                    ChronoUnit.DAYS.between(sheet.primoGiorno,date)
            updateGoogleCell(
                foglio,
                "R${INDICE_RIGA_PRIMO_ANIMATORE + anim.index}C$indice",
                content
            )
        }
        anim.updateDisponibilita(date, content)
    }
}
