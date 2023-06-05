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

@Composable
fun HomeButton(
    modifier: Modifier,
    label: String,
    onclick: () -> Unit = {},
    enabled: StateFlow<Boolean> = MutableStateFlow(true)
) {

    val _state by enabled.collectAsState()

    Button(
        modifier = modifier.height(HomeButtonHeight),
        onClick = onclick,
        enabled = _state
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