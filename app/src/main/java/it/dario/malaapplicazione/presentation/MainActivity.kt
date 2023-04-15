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
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.data.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.PresentationConstants.DATI_FATTURA
import it.dario.malaapplicazione.presentation.PresentationConstants.HOME
import it.dario.malaapplicazione.presentation.PresentationConstants.INSERISCI_DISPONIBILITA
import it.dario.malaapplicazione.presentation.PresentationConstants.VISUALIZZA_DISPONIBILITA
import it.dario.malaapplicazione.presentation.datiFattura.DatiFattura
import it.dario.malaapplicazione.presentation.home.Home
import it.dario.malaapplicazione.presentation.inserisciDisponibilita.InserisciDisponibilita
import it.dario.malaapplicazione.presentation.theme.MalaApplicazioneTheme
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.MalaViewModel
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.MalaViewModelFactory
import it.dario.malaapplicazione.presentation.visualizzaDisponibilita.VisualizzaDisponibilita

class MainActivity : ComponentActivity() {

    private val viewiewModel: MalaViewModel by viewModels {
        MalaViewModelFactory(
            repository = DisponibilitaRepository(MockDataSource()) //TODO modificare qui il data source
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                onNavigateToDatiFattura = { navController.navigate(DATI_FATTURA) }
                            )
                        }
                        composable(INSERISCI_DISPONIBILITA) { InserisciDisponibilita( viewModel = viewiewModel, navigateUp = navController::navigateUp) }
                        composable(VISUALIZZA_DISPONIBILITA) { VisualizzaDisponibilita(navigateUp = navController::navigateUp) }
                        composable(DATI_FATTURA) { DatiFattura(navigateUp = navController::navigateUp) }
                    }

                }
            }
        }
    }
}