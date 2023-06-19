package it.dario.malaapplicazione.presentation.sharedcomposable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.presentation.theme.MarginNormal


@Composable
fun ErrorDialog(
    messageId : Int,
) {
    Box(modifier = Modifier.width(280.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(bottom = 3.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(MarginNormal),
            contentAlignment = Alignment.Center,

            ) {
            Text(text = stringResource(id = messageId))
        }
    }
}


@Preview
@Composable
fun DialogPreview() {
    ErrorDialog(
        messageId = R.string.get_animatore_error,
    )
}