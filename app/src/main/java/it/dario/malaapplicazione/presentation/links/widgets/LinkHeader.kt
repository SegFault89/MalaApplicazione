package it.dario.malaapplicazione.presentation.links.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LinkHeader (text: String) {
    Text(text = text,
       modifier = Modifier.fillMaxWidth(),
       style = MaterialTheme.typography.headlineMedium
    )
}
