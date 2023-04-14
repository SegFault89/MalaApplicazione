package it.dario.malaapplicazione.presentation.sharedComposable

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MalaSpinner (modifier: Modifier = Modifier,
                     label: String,
                     options: List<T>,
                     getOptionLabel: (T) -> String,
                     onItemSelected: (T) -> Unit = {},
                     initiallyExpanded: Boolean = false) {

    var expanded by remember { mutableStateOf(initiallyExpanded) }
    var selectedOptionText by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ){
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            value = selectedOptionText,
            onValueChange = { selectedOptionText = it },
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
                            selectedOptionText = getOptionLabel(selectionOption)
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
    MalaSpinner(label = "MalaSpinner",
        options= options,
        getOptionLabel= SpinnerItem::getLabel)
}


private data class SpinnerItem (
    val text: String = "test",
    val value: Int = 1
) {
    fun getLabel (): String  {return text}
}