package it.dario.malaapplicazione.presentation.sharedcomposable

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MalaProgressIndicator(modifier: Modifier = Modifier, ready: StateFlow<Boolean>) {
    val hide by ready.collectAsState()

    if (!hide) {
        CircularProgressIndicator(modifier = modifier)
    }
}