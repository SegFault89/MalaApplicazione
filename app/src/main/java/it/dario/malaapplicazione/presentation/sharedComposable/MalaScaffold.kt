package it.dario.malaapplicazione.presentation.sharedComposable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

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