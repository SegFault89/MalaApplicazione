package it.dario.malaapplicazione.presentation.sharedcomposable

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.data.Constants.TAG
import it.dario.malaapplicazione.presentation.theme.HorizontalSpacingSmall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LabeledCheckbox(
    modifier: Modifier = Modifier,
    label: String = "Label",
    onCheckedChanged: (Boolean) -> Unit = {},
    toObserve: StateFlow<Boolean>
) {

    Log.d(TAG, "composing LabeledCheckbox $label")
    val _state by toObserve.collectAsState()


    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = _state,
            onCheckedChange = onCheckedChanged
        )

        Text(
            modifier = Modifier.padding(start = HorizontalSpacingSmall),
            text = label
        )
    }

}

@Preview
@Composable
fun LabeledCheckboxPreview(
    checked: Boolean = true,
    label: String = "Label",
    onCheckedChanged : (Boolean) -> Unit = {}
) {
    LabeledCheckbox(
        label = "Label",
        onCheckedChanged = {},
        toObserve = MutableStateFlow(true)
    )
}