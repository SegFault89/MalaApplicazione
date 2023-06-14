package it.dario.malaapplicazione.presentation.theme.shapes

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.LayoutDirection.Ltr
import androidx.compose.ui.unit.dp


class SegmentedButtonLeftShape(
    size: Dp
) : CornerBasedShape(
    topStart = CornerSize(size),
    topEnd = CornerSize(0.dp),
    bottomEnd = CornerSize(0.dp),
    bottomStart = CornerSize(size)
) {
    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection
    ) = if (topStart + topEnd + bottomEnd + bottomStart == 0.0f) {
        Outline.Rectangle(size.toRect())
    } else {
        Outline.Rounded(
            RoundRect(
                rect = size.toRect(),
                topLeft = CornerRadius(if (layoutDirection == Ltr) topStart else topEnd),
                topRight = CornerRadius(if (layoutDirection == Ltr) topEnd else topStart),
                bottomRight = CornerRadius(if (layoutDirection == Ltr) bottomEnd else bottomStart),
                bottomLeft = CornerRadius(if (layoutDirection == Ltr) bottomStart else bottomEnd)
            )
        )
    }

    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize
    ) = RoundedCornerShape(
        topStart = topStart,
        topEnd = topEnd,
        bottomEnd = bottomEnd,
        bottomStart = bottomStart
    )
}