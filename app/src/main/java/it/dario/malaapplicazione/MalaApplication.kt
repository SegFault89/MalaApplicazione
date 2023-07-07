package it.dario.malaapplicazione

import android.app.Application
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import it.dario.malaapplicazione.data.Constants.FIREBASE_URL

class MalaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        FirebaseDatabase.getInstance(FIREBASE_URL).setPersistenceEnabled(true)
    }
}
