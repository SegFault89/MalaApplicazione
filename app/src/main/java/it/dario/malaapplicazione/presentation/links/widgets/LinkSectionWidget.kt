package it.dario.malaapplicazione.presentation.links.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import it.dario.malaapplicazione.data.model.LinkSection
import it.dario.malaapplicazione.presentation.theme.VerticalSpacingSmall

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LinkSectionWidget(section: LinkSection) {

    var expanded by remember { mutableStateOf(true) }


    Column(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { expanded = !expanded }
            ),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = section.sezione,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
                    .align(Alignment.CenterVertically)
            )
        }

        AnimatedVisibility(visible = expanded, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(VerticalSpacingSmall)
            ) {
                section.elementi.sortedBy { it.order }.map { link ->
                    LinkButton(link = link)
                }
            }
        }

    }

}
