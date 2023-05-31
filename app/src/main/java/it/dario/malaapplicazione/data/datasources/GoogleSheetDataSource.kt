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
import it.dario.malaapplicazione.data.model.Animatore
import it.dario.malaapplicazione.data.model.Foglio
import it.dario.malaapplicazione.data.model.MalaFile
import it.dario.malaapplicazione.data.secret.Secret
import it.dario.malaapplicazione.domain.utils.LocalDateIterator
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.GeneralSecurityException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month


//TODO make it a singleton?
class GoogleSheetDataSource(val context: Context) : IDisponibilitaDataSource {


    private val APPLICATION_NAME = "Google Sheets API Java Quickstart"
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
    private val TOKENS_DIRECTORY_PATH = "tokens"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private val SCOPES = listOf(SheetsScopes.SPREADSHEETS)
    private val CREDENTIALS_FILE_PATH = "/credentials.json"

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): GoogleCredentials? {
                // Load client secrets.
        val `in`: InputStream = context.resources.openRawResource(R.raw.credential_secret)
        // Build flow and trigger user authorization request.

        return GoogleCredentials.fromStream(`in`).createScoped(SCOPES)
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
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



















    //region dati fittizi
    val malaFile = MalaFile(fogli = listOf("Ottobre'23", "Novembre'23"))

    val foglioOttobre = Foglio(
        meseString = "Ottobre",
        annoString = "23",
        meseInt = Month.OCTOBER.value,
        annoInt = 2023,
        primoGiorno = LocalDate.of(2023, Month.OCTOBER.value, 1),
        ultimoGiorno = LocalDate.of(2023, Month.NOVEMBER.value, 1).minusDays(1)
    )

    val foglioNovembre = Foglio(
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

    val darioOttobre = Animatore(
        nome = "Dario",
        cognome = "Trisconi",
        _domicilio = "Seregno",
        _auto = false,
        _adulti = true,
        _bambini = false,
        _note = "Datemidabbere"
    )
    val darioNovembre = Animatore(
        nome = "Dario",
        cognome = "Trisconi",
        _domicilio = "Seregno",
        _auto = false,
        _adulti = true,
        _bambini = false,
        _note = "Datemidabberedippiù"
    )

    val silviaOttobre = Animatore(
        nome = "Silvia",
        cognome = "Ratti",
        _domicilio = "Seregno",
        _auto = true,
        _adulti = true,
        _bambini = true,
        _note = "Mai più ad Ancona"
    )
    val silviaNovembre = Animatore(
        nome = "Silvia",
        cognome = "Ratti",
        _domicilio = "Seregno",
        _auto = true,
        _adulti = true,
        _bambini = true,
        _note = "Avevo detto mai più :("
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

    //private var foglioSelezionato: Foglio? = null
    override fun getFogli(): List<String> = malaFile.fogli

    override fun getAnimatori(foglio: String): List<Animatore> =
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
