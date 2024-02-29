package com.example.myapplication.uikit.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.uikit.theme.Black
import com.example.myapplication.uikit.theme.Gold
import com.example.myapplication.uikit.theme.LowBlack

/**
 * diameter = 2 * radius
 */
@Composable
fun CircularProgressCount(
    diameter: Dp,
    initialValue: Long,
    primaryColor: Color,
    secondaryColor: Color,
    textColor: Color,
    insideCircleColor: Color = Color.White,
    maxValue: Long,
    strokeSize: Float = 0.10f,
    textSizeCounter: Float = 0.3f,
    isTextFakeBold: Boolean = false,
    isCountDown: Boolean,
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    Box(
        modifier = Modifier
            .size(diameter)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            val textSizeCounterWidth = width * textSizeCounter
            val strokeSizeWidth = width * strokeSize
            val circleRadius = width / 2 - strokeSizeWidth
            val strokeRadius = circleRadius + strokeSizeWidth / 2

            circleCenter = Offset(x = width / 2f, y = height / 2f)

            drawCircle(
                color = insideCircleColor,
                radius = circleRadius,
                center = circleCenter
            )
            drawCircle(
                style = Stroke(
                    width = strokeSizeWidth
                ),
                color = secondaryColor,
                radius = strokeRadius,
                center = circleCenter
            )

            val sweepAngle =
                if (isCountDown && initialValue == -1L) 360f else (360f / maxValue) * initialValue.toFloat()

            drawArc(
                color = primaryColor,
                startAngle = 90f,
                sweepAngle = sweepAngle,
                style = Stroke(
                    width = strokeSizeWidth,
                    cap = StrokeCap.Round
                ),
                useCenter = false,
                size = Size(
                    width = strokeRadius * 2f,
                    height = strokeRadius * 2f
                ),
                topLeft = Offset(
                    (width - strokeRadius * 2f) / 2f,
                    (height - strokeRadius * 2f) / 2f
                )

            )

            val displayNumber = if (isCountDown && initialValue == -1L) maxValue else initialValue
            drawContext.canvas.nativeCanvas.apply {
                drawIntoCanvas {
                    drawText(
                        displayNumber.toString(),
                        circleCenter.x,
                        circleCenter.y + (textSizeCounterWidth / 3),
                        Paint().apply {
                            textSize = textSizeCounterWidth
                            textAlign = Paint.Align.CENTER
                            color = textColor.toArgb()
                            isFakeBoldText = isTextFakeBold
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    CircularProgressCount(
        diameter = 80.dp,
        initialValue = 15L,
        primaryColor = Gold,
        secondaryColor = LowBlack,
        textColor = Black,
        maxValue = 15,
        isCountDown = true
    )
}

@Preview(showBackground = true)
@Composable
fun Preview2() {
    CircularProgressCount(
        diameter = 80.dp,
        initialValue = 0,
        primaryColor = Gold,
        secondaryColor = LowBlack,
        textColor = Black,
        maxValue = 15,
        isCountDown = false
    )
}