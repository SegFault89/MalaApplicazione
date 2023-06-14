package it.dario.malaapplicazione.presentation.bugreportdialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.presentation.sharedcomposable.MalaSegmentedButton
import it.dario.malaapplicazione.presentation.theme.MarginBig

@Preview
@Composable
fun BugReportDialog(
    viewModel: BugReportViewModel = BugReportViewModel(),
    dismiss: () -> Unit = {}
) {

    val title by viewModel.title.collectAsState()
    val text by viewModel.description.collectAsState()
    val name by viewModel.name.collectAsState()
    val isFeature by viewModel.isFeature.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MarginBig)
                .padding(MarginBig),
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(
                    id = if (isFeature)
                        R.string.title_dialog_feature
                    else
                        R.string.title_dialog_bug),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(MarginBig))
            MalaSegmentedButton(
                modifier = Modifier.fillMaxWidth(),
                leftLabel = stringResource(id = R.string.feature),
                rightLabel = stringResource(id = R.string.bug),
                onLeftSelected = { viewModel.setIsFeature(true) },
                onRightSelected = { viewModel.setIsFeature(false) }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = viewModel::updateTitle,
                label = { Text(stringResource(id = R.string.titolo)) },
                singleLine = true,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = viewModel::updateDescription,
                label = { Text(stringResource(id = R.string.descrizione)) },
                singleLine = false,
                minLines = 5,
                maxLines = 5
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = viewModel::updateName,
                label = { Text(stringResource(id = R.string.name_optional)) },
                singleLine = true,
            )
        }
       Buttons(modifier = Modifier.align(Alignment.BottomCenter), viewModel = viewModel, dismiss)
    }
}

@Composable
fun Buttons(
    modifier: Modifier,
    viewModel: BugReportViewModel,
    dismiss: () -> Unit
) {
    val noDataMessage = R.string.bug_report_fill_data
    val errorMessage = R.string.bug_report_error
    val successMessage = R.string.bug_report_success
    val context = LocalContext.current

    val showToast =  { messageId : Int ->
        Toast.makeText(context, messageId, Toast.LENGTH_LONG).show()
    }
    Column(modifier = modifier) {
        Divider(color = MaterialTheme.colorScheme.onPrimaryContainer)
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    dismiss()
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = android.R.string.cancel),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(MaterialTheme.colorScheme.onPrimaryContainer),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (viewModel.canSendData()) {
                        viewModel.sendData(
                            onError = { showToast(errorMessage) },
                            onSuccess = { showToast(successMessage) },
                        )
                        dismiss()
                    } else {
                        showToast(noDataMessage)
                    }
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.send),
                //fontSize = 16.sp,
                //fontWeight = FontWeight.Normal,
                //color = Color.Black
            )
        }
    }
    }
}
