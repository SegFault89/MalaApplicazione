package it.dario.malaapplicazione.presentation.home.widgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.presentation.theme.HomeButtonHeight
import it.dario.malaapplicazione.presentation.theme.Typography

@Composable
fun HomeButton(
    modifier: Modifier,
    label: String,
    onclick: () -> Unit = {}
) {
    Button(
        modifier = modifier.height(HomeButtonHeight),
        onClick = onclick
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