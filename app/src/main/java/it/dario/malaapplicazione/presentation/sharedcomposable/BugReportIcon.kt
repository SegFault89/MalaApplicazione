package it.dario.malaapplicazione.presentation.sharedcomposable

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import it.dario.malaapplicazione.R


@Composable
fun BugReportIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_bug_report_24),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(id = R.string.bugreport)
        )
    }
}