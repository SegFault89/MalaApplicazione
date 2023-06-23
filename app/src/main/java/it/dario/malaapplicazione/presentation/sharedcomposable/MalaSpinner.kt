package it.dario.malaapplicazione.presentation.sharedcomposable

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.data.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MalaSpinner(
    modifier: Modifier = Modifier,
    label: String,
    options: List<T>,
    getOptionLabel: (T) -> String,
    onItemSelected: (T?) -> Unit = {},
    selected: String?,
    initiallyExpanded: Boolean = false,
    searchable: Boolean = false
) {

    Log.d(Constants.TAG, "composing MalaSpinner for $label")
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current


    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = !searchable,
            value = selected ?: query,
            onValueChange = { query = it; onItemSelected(null); expanded = true },
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),

            )

        val mOptions = if (searchable) {
            options.filter { getOptionLabel(it).contains(query, true) }
        } else {
            options
        }

        if (mOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {

                mOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(getOptionLabel(selectionOption)) },
                        onClick = {
                            expanded = false
                            onItemSelected(selectionOption)
                            focusManager.clearFocus()
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
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
