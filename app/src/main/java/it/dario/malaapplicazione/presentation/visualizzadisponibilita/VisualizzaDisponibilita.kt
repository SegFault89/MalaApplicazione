package it.dario.malaapplicazione.presentation.visualizzadisponibilita

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AndroidUiDispatcher.Companion.Main
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.data.datasources.MockDataSource
import it.dario.malaapplicazione.domain.repositories.DisponibilitaRepository
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaScaffold
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaSpinner
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.Others.spinnerModifier
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.widgets.VisualizzaDisponibilitaRange
import it.dario.malaapplicazione.presentation.visualizzadisponibilita.widgets.VisualizzaDisponibilitaSingolo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun VisualizzaDisponibilita(
    viewModel: VisualizzaDisponibilitaViewModel,
    navigateUp: () -> Unit,
    openBug: () -> Unit

) {

    val context = LocalContext.current

    MalaScaffold(
        label = stringResource(id = R.string.visualizza_disponibilita),
        navigateUp = navigateUp,
        additionalAction = listOf {
            RefreshButton {
                viewModel.refreshSheet(
                    onComplete = {
                        CoroutineScope(Main).launch {
                            Toast.makeText(
                                context,
                                R.string.foglio_aggiornato,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    onError = {
                        CoroutineScope(Main).launch {
                            Toast.makeText(
                                context,
                                R.string.error_aggiorna_foglio,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }
        },
        openBug = openBug
    )
    { contentPadding ->
        Content(Modifier.padding(contentPadding), viewModel)
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    viewModel: VisualizzaDisponibilitaViewModel
) {

    val currentFoglio by viewModel.foglioSelezionato.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MarginNormal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpinnerSection(
            viewModel = viewModel,
            foglio = currentFoglio,
        )

        if (currentFoglio != null) {
            Tabs(
                viewModel = viewModel,
                foglio = currentFoglio!!,
            )
        }
    }
}

@Composable
fun Tabs(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglio: String
) {
    var selected by remember { mutableIntStateOf(0) }
    val titles = listOf("Giorno", "Serie di giorni")
    Column {
        TabRow(selectedTabIndex = selected) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = selected == index,
                    onClick = { selected = index },
                    text = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                )
            }
        }

        val foglioIsLoading by viewModel.loadingFoglio.collectAsState()

        if (foglioIsLoading) {
            //se sto caricando
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            when (selected) {
                0 -> VisualizzaDisponibilitaSingolo(viewModel = viewModel, foglio = foglio)
                1 -> VisualizzaDisponibilitaRange(viewModel = viewModel, foglio = foglio)
            }
        }
    }
}

@Composable
fun SpinnerSection(
    viewModel: VisualizzaDisponibilitaViewModel,
    foglio: String?,
) {

    // Spinner foglio
    MalaSpinner(
        modifier = spinnerModifier,
        label = stringResource(id = R.string.seleziona_foglio),
        options = viewModel.getFogli(),
        getOptionLabel = { it },
        selected = foglio,
        onItemSelected = viewModel::updateFoglioSelezionato
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewContent() {
    val mock = MockDataSource()
    Content(
        viewModel = VisualizzaDisponibilitaViewModel(
            DisponibilitaRepository(
                mock
            )
        ).apply {
            updateFoglioSelezionato(mock.foglioNovembre.label)
        }
    )
}

@Composable
fun RefreshButton(
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_refresh_24),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(id = R.string.bugreport)
        )
    }
}
