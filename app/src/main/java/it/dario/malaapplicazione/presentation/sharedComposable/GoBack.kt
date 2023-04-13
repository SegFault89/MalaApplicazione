package it.dario.malaapplicazione.presentation.sharedComposable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import it.dario.malaapplicazione.R

@Composable
fun GoBack (navigateUp: ()->Unit) {
    IconButton(onClick = navigateUp) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.indietro)
        )
    }
}