package it.dario.malaapplicazione.presentation.links

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.presentation.links.widgets.LinkSectionWidget
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaScaffold
import it.dario.malaapplicazione.presentation.theme.MarginBig
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.VerticalSpacingNormal

/**
 * Lista di link utili, suddivisi per sezioni
 */
@Preview(showBackground = true)
@Composable
fun LinkUtili(
    viewModel: LinkUtiliViewModel = LinkUtiliViewModel(),
    navigateUp: () -> Unit = {},
    openBug: () -> Unit = {}
) {
    MalaScaffold(
        label = stringResource(id = R.string.links),
        navigateUp = navigateUp,
        openBug = openBug,
    ) { contentPadding ->

        val links by viewModel.links.observeAsState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = MarginBig, vertical = MarginNormal),
            verticalArrangement = Arrangement.spacedBy(VerticalSpacingNormal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            links?.sortedBy { it.order }?.forEach { section ->

                item {  LinkSectionWidget(section = section) }
            }
        }
    }
}
