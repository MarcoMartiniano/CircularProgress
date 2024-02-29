package com.example.myapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.presentation.MainScreenViewModel
import com.example.myapplication.uikit.components.CircularProgressCount
import com.example.myapplication.uikit.theme.Black
import com.example.myapplication.uikit.theme.Gold
import com.example.myapplication.uikit.theme.LowBlack
import com.example.myapplication.uikit.theme.MyApplicationTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainScreenFactory() {
    val viewModel: MainScreenViewModel = koinViewModel()
    MainScreen(viewModel = viewModel)
}

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val timerCountUp = viewModel.timerCountUp.collectAsState()
    val timerCountDown = viewModel.timerCountDown.collectAsState()

    val time = 10L

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Circular Progress with Count Up")
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressCount(
                diameter = 80.dp,
                initialValue = timerCountUp.value,
                primaryColor = Gold,
                secondaryColor = LowBlack,
                textColor = Black,
                maxValue = time,
                strokeSize = 0.13f,
                textSizeCounter = 0.3f,
                isTextFakeBold = true,
                isCountDown = false
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.stopCountUpTimer() })
                {
                    Text(text = "Stop", modifier = Modifier.padding(horizontal = 12.dp))
                }
                Button(
                    onClick = { viewModel.startCountUpTimer(time) })
                {
                    Text(text = "Start", modifier = Modifier.padding(horizontal = 12.dp))
                }
                Button(
                    onClick = { viewModel.pauseCountUpTimer() })
                {
                    Text(text = "Pause", modifier = Modifier.padding(horizontal = 12.dp))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Circular Progress with Count Down")
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressCount(
                diameter = 80.dp,
                initialValue = timerCountDown.value,
                primaryColor = Gold,
                secondaryColor = LowBlack,
                textColor = Black,
                maxValue = time,
                strokeSize = 0.13f,
                textSizeCounter = 0.3f,
                isCountDown = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.stopCountDownTimer() })
                {
                    Text(text = "Stop", modifier = Modifier.padding(horizontal = 12.dp))
                }
                Button(
                    onClick = { viewModel.startCountDownTimer(time) })
                {
                    Text(text = "Start", modifier = Modifier.padding(horizontal = 12.dp))
                }
                Button(
                    onClick = { viewModel.pauseCountDownTimer() })
                {
                    Text(text = "Pause", modifier = Modifier.padding(horizontal = 12.dp))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "CircularProgressWithGesture")
            Spacer(modifier = Modifier.height(12.dp))
            //CircularProgressWithGesture
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    MyApplicationTheme {
        MainScreen(viewModel = koinViewModel())
    }
}