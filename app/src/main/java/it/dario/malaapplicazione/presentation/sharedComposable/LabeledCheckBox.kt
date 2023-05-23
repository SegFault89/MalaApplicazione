package it.dario.malaapplicazione.presentation.sharedComposable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.presentation.theme.HorizontalSpacingSmall

@Composable
fun LabeledCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean = true,
    label: String = "Label",
    onCheckedChanged : (Boolean) -> Unit = {}
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
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
        checked = true,
        label = "Label",
        onCheckedChanged = {}
    )
}