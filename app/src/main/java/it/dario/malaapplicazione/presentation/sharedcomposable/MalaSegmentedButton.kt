package it.dario.malaapplicazione.presentation.sharedcomposable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.presentation.theme.shapes.SegmentedButtonLeftShape
import it.dario.malaapplicazione.presentation.theme.shapes.SegmentedButtonRightShape



@Composable
fun MalaSegmentedButton (
    modifier: Modifier = Modifier,
    leftLabel: String,
    rightLabel: String,
    onLeftSelected: () -> Unit,
    onRightSelected: () -> Unit
) {
    val colorSelected  = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.surface)
    val colorUnselected  = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.33f),
        containerColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.33f))
    val borderSelected = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
    val borderUnselected = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.33f))

    var selectedIndex by remember { mutableStateOf(0) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = { if (selectedIndex!= 0) { selectedIndex = 0; onLeftSelected()} },
            border = if (selectedIndex == 0) borderSelected else borderUnselected,
            shape = SegmentedButtonLeftShape(30.dp),
            colors = if (selectedIndex == 0) colorSelected else colorUnselected,
        ) {
            Text(text = leftLabel)
        }
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = { if (selectedIndex!= 1) { selectedIndex = 1; onRightSelected()} },
            border = if (selectedIndex == 1) borderSelected else borderUnselected,
            shape = SegmentedButtonRightShape(30.dp),
            colors = if (selectedIndex == 1) colorSelected else colorUnselected,
        ) {
            Text(text = rightLabel)
        }
    }
}

@Preview
@Composable
fun MalaSegmentedButtonPreview () {
    MalaSegmentedButton(
        leftLabel = "ButtonLeft",
        rightLabel = "ButtonRight",
        onLeftSelected = {},
        onRightSelected = {}
    )
}