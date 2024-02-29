package com.example.myapplication.uikit.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.uikit.theme.Black
import com.example.myapplication.uikit.theme.Gold
import com.example.myapplication.uikit.theme.LowBlack
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

/**
 * diameter = 2 * radius
 */
@Composable
fun CircularProgressCount(
    diameter: Dp,
    initialValue: Long,
    insideCircleColor: Color = Color.White,
    outsideCircleColor: Color,
    strokeColor: Color,
    textColor: Color,
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
                color = outsideCircleColor,
                radius = strokeRadius,
                center = circleCenter
            )

            val sweepAngle =
                if (isCountDown && initialValue == -1L) 360f else (360f / maxValue) * initialValue.toFloat()

            drawArc(
                color = strokeColor,
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

@Composable
fun CircularProgressGesture(
    diameter: Dp,
    initialValue: Int,
    insideCircleColor: Color,
    outsideCircleColor: Color,
    strokeColor: Color,
    textColor: Color,
    minValue: Int = 0,
    maxValue: Int = 100,
    strokeSize: Float = 0.10f,
    textSizeCounter: Float = 0.22f,
    onPositionChange: (Int) -> Unit,
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var positionValue by remember {
        mutableIntStateOf(initialValue)
    }

    var changeAngle by remember {
        mutableFloatStateOf(0f)
    }

    var dragStartedAngle by remember {
        mutableFloatStateOf(0f)
    }

    var oldPositionValue by remember {
        mutableIntStateOf(initialValue)
    }

    Box(
        modifier = Modifier.size(diameter)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStartedAngle = -atan2(
                                x = circleCenter.y - offset.y,
                                y = circleCenter.x - offset.x
                            ) * (180f / PI).toFloat()
                            dragStartedAngle = (dragStartedAngle + 180f).mod(360f)
                        },
                        onDrag = { change, _ ->
                            var touchAngle = -atan2(
                                x = circleCenter.y - change.position.y,
                                y = circleCenter.x - change.position.x
                            ) * (180f / PI).toFloat()
                            touchAngle = (touchAngle + 180f).mod(360f)

                            val currentAngle = oldPositionValue * 360f / (maxValue - minValue)
                            changeAngle = touchAngle - currentAngle

                            val lowerThreshold = currentAngle - (360f / (maxValue - minValue) * 5)
                            val higherThreshold = currentAngle + (360f / (maxValue - minValue) * 5)

                            if (dragStartedAngle in lowerThreshold..higherThreshold) {
                                positionValue =
                                    (oldPositionValue + (changeAngle / (360f / (maxValue - minValue))).roundToInt())
                            }
                        },
                        onDragEnd = {
                            oldPositionValue = positionValue
                            onPositionChange(positionValue)
                        }
                    )
                }
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
                color = outsideCircleColor,
                radius = strokeRadius,
                center = circleCenter
            )

            drawArc(
                color = strokeColor,
                startAngle = 90f,
                sweepAngle = (360f / maxValue) * positionValue.toFloat(),
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
            drawContext.canvas.nativeCanvas.apply {
                drawIntoCanvas {
                    drawText(
                        "$positionValue %",
                        circleCenter.x,
                        circleCenter.y + (textSizeCounterWidth / 3),
                        Paint().apply {
                            textSize = textSizeCounterWidth
                            textAlign = Paint.Align.CENTER
                            color = textColor.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CircularProgressCountDown() {
    CircularProgressCount(
        diameter = 80.dp,
        initialValue = 15,
        insideCircleColor = LowBlack,
        outsideCircleColor = Color.LightGray,
        strokeColor = Gold,
        textColor = Black,
        maxValue = 15,
        strokeSize = 0.13f,
        textSizeCounter = 0.3f,
        isCountDown = true
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCircularProgressCountUp() {
    CircularProgressCount(
        diameter = 80.dp,
        initialValue = 0,
        insideCircleColor = LowBlack,
        outsideCircleColor = Black,
        strokeColor = Gold,
        textColor = Black,
        maxValue = 15,
        strokeSize = 0.13f,
        textSizeCounter = 0.3f,
        isCountDown = false
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCircularProgressGesture() {
    CircularProgressGesture(
        diameter = 200.dp,
        initialValue = 10,
        insideCircleColor = Color.Gray,
        outsideCircleColor = Color.DarkGray,
        strokeColor = Color.Yellow,
        textColor = Color.White,
        onPositionChange = { position ->

        }
    )
}