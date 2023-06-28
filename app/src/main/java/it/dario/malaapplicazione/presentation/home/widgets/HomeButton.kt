package it.dario.malaapplicazione.presentation.home.widgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.presentation.theme.HomeButtonHeight
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.Typography
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * tasti che vengono visualizzati nella homePage
 * @param modifier modifier
 * @param label testo sul tasto
 * @param description testo in piccolo con una breve descrizione
 * @param onclick funzione chiamata al click
 * @param enabled StateFlow che controlla l'abilitazione del tasto
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeButton(
    modifier: Modifier,
    label: String,
    description: String,
    onclick: () -> Unit = {},
    enabled: StateFlow<Boolean> = MutableStateFlow(true)
) {

    val state by enabled.collectAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(HomeButtonHeight),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = MaterialTheme.shapes.medium,
        enabled = state,
        onClick = onclick,

        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MarginNormal)
        )
        {
            Text(
                text = label,
                style = Typography.headlineSmall,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Text(
                text = description,
                style = Typography.labelMedium,
                modifier = Modifier.align(BottomEnd),
                textAlign = TextAlign.End
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HomeButtonLightPreview() {
    HomeButton(modifier = Modifier.width(400.dp), label = "HomeButton Light", description = "HomeButton Description")
}

