package it.dario.malaapplicazione.presentation.bugreportdialog

import android.util.Log
import androidx.lifecycle.ViewModel
import it.dario.malaapplicazione.data.Constants.SUCCESS_RESPONSE
import it.dario.malaapplicazione.data.Constants.TAG
import it.dario.malaapplicazione.data.secret.Secret
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class BugReportViewModel : ViewModel() {
    private val _title = MutableStateFlow("")
    private val _description = MutableStateFlow("")
    private val _name = MutableStateFlow("")
    private val _isFeature = MutableStateFlow(true)

    val title = _title.asStateFlow()
    val description = _description.asStateFlow()
    val name = _name.asStateFlow()
    val isFeature = _isFeature.asStateFlow()

    fun canSendData(): Boolean {
        return title.value.isNotBlank() && description.value.isNotBlank()
    }

    private val ioDispatcher = IO
    private val mainDispatcher = Main


    fun sendData(onError: () -> Unit, onSuccess: () -> Unit) = CoroutineScope(ioDispatcher).launch{
        val url =
            URL("https://api.github.com/repos/${Secret.GITHUB_OWNER}/${Secret.GITHUB_REPO}/issues")
        val body =
            "${description.value.trim()}${if (name.value.isNotBlank()) "\n\n${name.value.trim()}" else ""}"
            .replace("\n", "\\n")

        val postData = """
        {
            "title": "${title.value.trim()}",
            "body": "$body",
            "labels": ["${if (isFeature.value) "enhancement" else "bug"}"]
        }    
        """.trimIndent()

        val conn = withContext(ioDispatcher) {
            url.openConnection()
        } as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Accept", "application/vnd.github+json")
        conn.setRequestProperty("Authorization", "Bearer ${Secret.GITHUB_TOKEN}")
        conn.setRequestProperty("X-GitHub-Api-Version", "2022-11-28")

        conn.useCaches = false


        DataOutputStream(conn.outputStream).use { it.writeBytes(postData) }
        Log.d(TAG, "Responsecode = ${conn.responseCode}")
        val success = conn.responseCode == SUCCESS_RESPONSE
        withContext(mainDispatcher) {
            if (success) {
                onSuccess()
            } else {
                onError()
            }
        }
    }

    fun setIsFeature(feature: Boolean) {
        _isFeature.value = feature
    }

    fun updateTitle(title: String) {
        _title.value = title
    }

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun updateName(name: String) {
        _name.value = name
    }

}
