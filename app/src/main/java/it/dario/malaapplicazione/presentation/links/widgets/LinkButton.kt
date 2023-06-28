package it.dario.malaapplicazione.presentation.links.widgets

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.dario.malaapplicazione.data.model.Link
import it.dario.malaapplicazione.presentation.theme.MarginNormal
import it.dario.malaapplicazione.presentation.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkButton (link: Link) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = MaterialTheme.shapes.medium,
        onClick =  {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link.link)))
        }
        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MarginNormal)
        )
        {
            Text(
                text = link.nome,
                style = Typography.bodyMedium,
                modifier = Modifier.align(Alignment.TopStart)
            )
            link.descrizione?.let {
                Text(
                    text = it,
                    style = Typography.bodySmall,
                    modifier = Modifier.align(Alignment.BottomEnd),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
