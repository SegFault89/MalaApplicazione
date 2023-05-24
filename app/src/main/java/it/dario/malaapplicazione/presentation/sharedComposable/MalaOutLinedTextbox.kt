package it.dario.malaapplicazione.presentation.sharedComposable

import android.util.Log
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.data.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MalaOutlinedTextBox(
    modifier: Modifier = Modifier,
    label: String,
    toObserve: StateFlow<String>,
    onValueChangeListener: (String) -> Unit
) {

    Log.d(Constants.TAG, "composing MalaOutlinedTextBox $label")
    val _state by toObserve.collectAsState()

    OutlinedTextField(
        modifier = modifier,
        value = _state,
        onValueChange = onValueChangeListener,
        label = { Text(label) },
    )
}

@Preview(showBackground = true)
@Composable
fun MalaOutlinedTextBoxPreview() {
    MalaOutlinedTextBox(label = "Mala Outlined Text Box", toObserve = MutableStateFlow("text"), onValueChangeListener = {})
}
