package it.dario.malaapplicazione.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.presentation.bugreportdialog.BugReportDialog
import it.dario.malaapplicazione.presentation.bugreportdialog.BugReportViewModel
import it.dario.malaapplicazione.presentation.datifattura.DatiFattura
import it.dario.malaapplicazione.presentation.home.Home
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.InserisciDisponibilita
import it.dario.malaapplicazione.presentation.inseriscidisponibilita.InserisciDisponibilitaViewModel
import it.dario.malaapplicazione.presentation.sharedcomposable.ErrorDialog
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilita
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.VisualizzaDisponibilitaViewModel

@Composable
fun MalaNavHost(
    navController: NavHostController,
    viewModel: MalaViewModel,
    inserisciDisponibilitaViewModel: InserisciDisponibilitaViewModel,
    visualizzaDisponibilitaViewModel: VisualizzaDisponibilitaViewModel
) {
    NavHost(navController = navController, startDestination = PresentationConstants.HOME) {
        composable(PresentationConstants.HOME) {
            Home(
                viewModel = viewModel,
                onNavigateToInserisci = {
                    navController.navigate(
                        PresentationConstants.INSERISCI_DISPONIBILITA
                    )
                },
                onNavigateToVisualizza = {
                    navController.navigate(
                        PresentationConstants.VISUALIZZA_DISPONIBILITA
                    )
                },
                onNavigateToDatiFattura = {
                    navController.navigate(
                        PresentationConstants.DATI_FATTURA
                    )
                },
                openBug = { navController.navigate(PresentationConstants.BUG_REPORT) }
            )
        }
        composable(PresentationConstants.INSERISCI_DISPONIBILITA) {
            InserisciDisponibilita(
                viewModel = inserisciDisponibilitaViewModel,
                navigateUp = navController::navigateUp,
                openBug = { navController.navigate(PresentationConstants.BUG_REPORT) }
            )
        }
        composable(PresentationConstants.VISUALIZZA_DISPONIBILITA) {
            VisualizzaDisponibilita(
                viewModel = visualizzaDisponibilitaViewModel,
                navigateUp = navController::navigateUp,
                openBug = { navController.navigate(PresentationConstants.BUG_REPORT) }
            )
        }
        composable(PresentationConstants.DATI_FATTURA) {
            DatiFattura(
                navigateUp = navController::navigateUp,
                openBug = { navController.navigate(PresentationConstants.BUG_REPORT) }
            )
        }
        dialog(
            "${PresentationConstants.ERROR_DIALOG}/{${PresentationConstants.ERROR_MESSAGE_ID}}",
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            ErrorDialog(
                messageId = it.arguments?.getString(PresentationConstants.ERROR_MESSAGE_ID)?.toIntOrNull()
                    ?: R.string.generic_error
            )
        }
        dialog(
            PresentationConstants.BUG_REPORT,
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            BugReportDialog(
                viewModel = BugReportViewModel(),
                dismiss = navController::navigateUp
            )
        }
    }

}
