package it.dario.malaapplicazione.presentation.inseriscidisponibilita.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import it.dario.malaapplicazione.R
import it.dario.malaapplicazione.presentation.theme.DisponibileGreen
import it.dario.malaapplicazione.presentation.theme.DisponibileRed
import it.dario.malaapplicazione.presentation.theme.DisponibileYellow
import it.dario.malaapplicazione.presentation.theme.MarginBig
import it.dario.malaapplicazione.presentation.theme.MarginNormal

@Preview
@Composable
fun BottomSheetLegenda() {

    val firstWeight = .2f
    val secondWeight = .8f
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = MarginBig, start = MarginBig, end = MarginBig),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.colors),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )

        Row(Modifier.fillMaxWidth()) {
            ColoredBox(modifier = Modifier.weight(firstWeight), color = DisponibileGreen)
            Text(
                modifier = Modifier
                    .weight(secondWeight)
                    .align(CenterVertically), text = stringResource(id = R.string.disponibile)
            )
        }
        Row(Modifier.fillMaxWidth()) {
            ColoredBox(modifier = Modifier.weight(firstWeight), color = DisponibileRed)
            Text(
                modifier = Modifier
                    .weight(secondWeight)
                    .align(CenterVertically), text = stringResource(id = R.string.non_disponibile)
            )
        }
        Row(Modifier.fillMaxWidth()) {
            ColoredBox(modifier = Modifier.weight(firstWeight), color = DisponibileYellow)
            Text(
                modifier = Modifier
                    .weight(secondWeight)
                    .align(CenterVertically),
                text = stringResource(id = R.string.disponibile_riserva)
            )
        }


        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.functions),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )

        Row(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .weight(4f)
                    .align(CenterVertically)
                    .padding(MarginNormal), text = stringResource(id = R.string.press)
            )
            Text(
                modifier = Modifier
                    .weight(6f)
                    .align(CenterVertically)
                    .padding(MarginNormal), text = stringResource(R.string.change_disponibility)
            )
        }
        Row(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .weight(4f)
                    .align(CenterVertically)
                    .padding(MarginNormal), text = stringResource(id = R.string.long_press)
            )
            Text(
                modifier = Modifier
                    .weight(6f)
                    .align(CenterVertically)
                    .padding(MarginNormal),
                text = stringResource(id = R.string.insert_conditional_disponibility)
            )
        }
    }
}


@Composable
fun ColoredBox(modifier: Modifier, color: Color) {
    Box(
        modifier = modifier
            .padding(MarginNormal)
            .aspectRatio(1f)

    ) {
        Box(
            Modifier
                .background(color = color, shape = CircleShape)
                .fillMaxSize()
                .align(Alignment.Center)
        )
    }
}

/*
LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .padding(MarginNormal),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(gridItems) {
                Text(text = stringResource(id = it))
            }
        }
    }
*/