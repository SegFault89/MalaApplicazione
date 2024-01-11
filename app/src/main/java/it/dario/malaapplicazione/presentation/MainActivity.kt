package it.dario.malaapplicazione.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
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

    val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        viewModel.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAuthState().observe(this) { userSignedIn ->
            if (userSignedIn) {
                //TODO initial loading

                CoroutineScope(Dispatchers.IO).launch {
                    dataSource.apply {
                        setup(baseContext)
                    }
                }

                // TODO REWORK ^^^^

            } else {
                viewModel.requireSignIn(signInLauncher)
            }

        }



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
}
