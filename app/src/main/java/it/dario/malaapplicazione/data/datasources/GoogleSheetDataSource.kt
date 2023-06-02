package it.dario.malaapplicazione.data.datasources

import android.content.Context
import android.util.Log
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.CellData
import com.google.api.services.sheets.v4.model.Sheet
import com.google.api.services.sheets.v4.model.SheetProperties
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.Constants.TAG
import it.dario.malaapplicazione.data.datasources.GoogleSheetConstants.COLONNE_NOME_COGNOME_ANIMATORI
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import it.dario.malaapplicazione.data.model.MalaFile
import it.dario.malaapplicazione.data.secret.Secret
import it.dario.malaapplicazione.data.secret.Secret.GOOLE_SPREADSHEET
import it.dario.malaapplicazione.domain.utils.LocalDateIterator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.GeneralSecurityException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month


object GoogleSheetDataSource : IDisponibilitaDataSource {


    private val APPLICATION_NAME = "MalaApplicazione" //TODO mettere in buildConfig?
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    private val SCOPES = listOf(SheetsScopes.SPREADSHEETS)

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean>
        get() = _isReady

    private lateinit var service: Sheets

    private lateinit var malaFile: MalaFile

    override fun setup(context: Context) {

        val `in`: InputStream = context.resources.openRawResource(R.raw.credential_secret)
        // Build flow and trigger user authorization request.
        val credentials = GoogleCredentials.fromStream(`in`).createScoped(SCOPES)

        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME)
            .build()

        malaFile = MalaFile(fogli = getSheets())

        _isReady.value = true
    }

    private fun getSheets(): List<String> =
        service.spreadsheets().get(GOOLE_SPREADSHEET).apply { includeGridData = false }
            .execute().sheets.map { it.properties.title }.toList() //TODO filter

    override fun getFogli(): List<String> = malaFile.fogli

    private suspend fun fetchFoglio(name: String) : Foglio {
        val giorniNelFoglio = service.spreadsheets().values()[GOOLE_SPREADSHEET, "$name!"].execute()
        Log.d(TAG, "ROW LENGTH = ${(rows.getValues().first().size)}")
        rows.getValues().first().forEachIndexed { i, v ->
            Log.d(TAG, "column $i = $v")
        }

        val animatoriFromFile = service.spreadsheets().values()[GOOLE_SPREADSHEET, "$name!$COLONNE_NOME_COGNOME_ANIMATORI"].execute()
        /*Log.d(TAG, "ROW NUM = ${animatoriFromFile.getValues().size}")
        columns.getValues().forEachIndexed { i, v ->
            try {
                Log.d(TAG, "row $i = ${v[0]}")
            } catch (t: Throwable) {
                Log.d(TAG, "row $i empty")
            }
        }*/
        //val foglio = Foglio()
    }

    override suspend fun getAnimatori(foglio: String): List<Animatore> {
        //controllo se il foglio esiste già nel malaFIle
        val mFoglio = malaFile.malaFogli[foglio] ?: fetchFoglio(foglio)
        //controllo quanto è passato dal download
        //se è abbastanza fresco uso direttamente lui
        //altrimenti lo riprendo dall'internet
        //restituisco la lista degli animatori

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

            //ROW SIZE
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

    override fun updateDomicilio(foglio: String, animatore: String, value: String) {
        getAnimatore(foglio, animatore).updateDomicilio(value)
    }

    override fun updateAuto(foglio: String, animatore: String, value: Boolean) {
        getAnimatore(foglio, animatore).updateAuto(value)
    }

    override fun updateBambini(foglio: String, animatore: String, value: Boolean) {
        getAnimatore(foglio, animatore).updateBambini(value)
    }

    override fun updateAdulti(foglio: String, animatore: String, value: Boolean) {
        getAnimatore(foglio, animatore).updateAdulti(value)
    }

    override fun updateNote(foglio: String, animatore: String, value: String) {
        getAnimatore(foglio, animatore).updateNote(value)
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

    override fun setDisponibilita(
        foglio: String,
        animatore: String,
        date: LocalDate,
        content: String
    ) {
        getAnimatore(foglio, animatore).updateDisponibilita(date, content)
    }
}
