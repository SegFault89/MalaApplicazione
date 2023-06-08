package it.dario.malaapplicazione.presentation.home.widgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.presentation.theme.HomeButtonHeight
import it.dario.malaapplicazione.presentation.theme.Typography
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * tasti che vengono visualizzati nella homePage
 * @param modifier modifier
 * @param label testo sul tasto
 * @param onclick funzione chiamata al click
 * @param enabled StateFlow che controlla l'abilitazione del tasto
 */
@Composable
fun HomeButton(
    modifier: Modifier,
    label: String,
    onclick: () -> Unit = {},
    enabled: StateFlow<Boolean> = MutableStateFlow(true)
) {

    val state by enabled.collectAsState()

    Button(
        modifier = modifier.height(HomeButtonHeight),
        onClick = onclick,
        enabled = state
    ) {
        Text(text = label, style = Typography.titleLarge)
    }
}



@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HomeButtonLightPreview() {
    HomeButton(modifier = Modifier.width(400.dp), label = "HomeButton Light")
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeButtonDarkPreview() {
    HomeButton(modifier = Modifier.width(400.dp), label = "HomeButton Dark")
}