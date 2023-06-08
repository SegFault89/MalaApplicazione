package it.dario.malaapplicazione.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.dario.malaapplicazione.BuildConfig
import it.dario.malaapplicazione.data.datasources.GoogleSheetDataSource
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.PresentationConstants.DATI_FATTURA
import it.dario.malaapplicazione.presentation.PresentationConstants.HOME
import it.dario.malaapplicazione.presentation.PresentationConstants.INSERISCI_DISPONIBILITA
import it.dario.malaapplicazione.presentation.PresentationConstants.VISUALIZZA_DISPONIBILITA
import it.dario.malaapplicazione.presentation.datifattura.DatiFattura
import it.dario.malaapplicazione.presentation.home.Home
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.InserisciDisponibilita
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.InserisciDisponibilitaViewModel
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.InserisciDisponibilitaViewModelFactory
import it.dario.malaapplicazione.presentation.theme.MalaApplicazioneTheme
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilita
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
        CoroutineScope(Dispatchers.IO).launch { dataSource.setup(baseContext) }
        setContent {
            MalaApplicazioneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = HOME) {
                        composable(HOME) {
                            Home(
                                viewModel = viewModel,
                                onNavigateToInserisci = {
                                    navController.navigate(
                                        INSERISCI_DISPONIBILITA
                                    )
                                },
                                onNavigateToVisualizza = {
                                    navController.navigate(
                                        VISUALIZZA_DISPONIBILITA
                                    )
                                },
                                onNavigateToDatiFattura = {
                                    navController.navigate(
                                        DATI_FATTURA
                                    )
                                }
                            )
                        }
                        composable(INSERISCI_DISPONIBILITA) {
                            InserisciDisponibilita(
                                viewModel = inserisciDisponibilitaViewModel,
                                navigateUp = navController::navigateUp
                            )
                        }
                        composable(VISUALIZZA_DISPONIBILITA) {
                            VisualizzaDisponibilita(
                                viewModel = visualizzaDisponibilitaViewModel,
                                navigateUp = navController::navigateUp
                            )
                        }
                        composable(DATI_FATTURA) { DatiFattura(navigateUp = navController::navigateUp) }
                    }

                }
            }
        }
    }
}