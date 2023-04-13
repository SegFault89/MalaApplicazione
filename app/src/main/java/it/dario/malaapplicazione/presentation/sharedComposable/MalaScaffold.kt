package it.dario.malaapplicazione.presentation.sharedComposable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.dario.malaapplicazione.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MalaScaffold(label: String, navigateUp: ()-> Unit,  content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = label)
                },
                navigationIcon = { GoBack(navigateUp = navigateUp) }
            )
        }) { contentPadding ->
        content(contentPadding)
    }
}