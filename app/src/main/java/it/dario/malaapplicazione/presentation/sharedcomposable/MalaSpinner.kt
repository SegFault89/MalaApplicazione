package it.dario.malaapplicazione.presentation.sharedcomposable

import android.util.Log
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.data.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MalaSpinner(
    modifier: Modifier = Modifier,
    label: String,
    options: List<T>,
    getOptionLabel: (T) -> String,
    onItemSelected: (T) -> Unit = {},
    selected: String?,
    initiallyExpanded: Boolean = false
) {

    Log.d(Constants.TAG, "composing MalaSpinner for $label")
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = modifier.menuAnchor(),
            readOnly = true,
            value = selected ?: "",
            onValueChange = { },
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),

        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(getOptionLabel(selectionOption)) },
                    onClick = {
                        expanded = false
                        onItemSelected(selectionOption)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpinner() {
    val options = listOf(SpinnerItem("1", 1), SpinnerItem("2", 2))
    MalaSpinner(
        label = "MalaSpinner",
        options = options,
        getOptionLabel = SpinnerItem::label,
        selected = null
    )
}

private data class SpinnerItem(
    val text: String = "test",
    val value: Int = 1
) {
    val label: String get() = text
}