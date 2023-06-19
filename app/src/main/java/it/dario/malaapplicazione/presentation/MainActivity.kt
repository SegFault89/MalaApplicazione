package it.dario.malaapplicazione.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import it.dario.malaapplicazione.BuildConfig
import it.dario.malaapplicazione.data.datasources.GoogleSheetDataSource
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.domain.DatasourceErrorHandler
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.PresentationConstants.ERROR_DIALOG
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.InserisciDisponibilitaViewModel
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.InserisciDisponibilitaViewModelFactory
import it.dario.malaapplicazione.presentation.theme.MalaApplicazioneTheme
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilitaViewModel
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilitaViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val dataSource = if (BuildConfig.MOCK_DATA) {
        MockDataSource()
    } else {
        GoogleSheetDataSource
    }

    private val viewModel: MalaViewModel by viewModels {
        MalaViewModelFactory(
            repository = DisponibilitaRepository(datasource = dataSource)
        )
    }
    private val inserisciDisponibilitaViewModel: InserisciDisponibilitaViewModel by viewModels {
        InserisciDisponibilitaViewModelFactory(
            repository = DisponibilitaRepository(datasource = dataSource)
        )
    }

    private val visualizzaDisponibilitaViewModel: VisualizzaDisponibilitaViewModel by viewModels {
        VisualizzaDisponibilitaViewModelFactory(
            repository = DisponibilitaRepository(datasource = dataSource)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.apply {
                setup(baseContext)
            }
        }
        askNotificationPermission()
        setContent {
            MalaApplicazioneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    dataSource.setErrorHandler(DatasourceErrorHandler { res: Int ->
                        CoroutineScope(
                            Dispatchers.Main
                        ).launch {
                            navController.navigate("$ERROR_DIALOG/$res")
                        }
                    })

                    MalaNavHost(
                        navController = navController,
                        viewModel = viewModel,
                        inserisciDisponibilitaViewModel = inserisciDisponibilitaViewModel,
                        visualizzaDisponibilitaViewModel = visualizzaDisponibilitaViewModel
                    )

                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
