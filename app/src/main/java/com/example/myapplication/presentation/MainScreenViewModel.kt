package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    private val _timerCountUp = MutableStateFlow(0L)
    val timerCountUp = _timerCountUp.asStateFlow()

    private val _timerCountDown = MutableStateFlow(-1L)
    val timerCountDown = _timerCountDown.asStateFlow()

    private var timerCountUpJob: Job? = null
    private var timerCountDownJob: Job? = null

    fun startCountUpTimer(time: Long) {
        stopCountUpTimer()
        timerCountUpJob = viewModelScope.launch {
            while (time > _timerCountUp.value) {
                delay(1000)
                _timerCountUp.value++
            }
            timerCountUpJob?.cancel()
        }
    }

    fun pauseCountUpTimer() {
        timerCountUpJob?.cancel()
    }

    fun stopCountUpTimer() {
        _timerCountUp.value = 0
        timerCountUpJob?.cancel()
    }

    fun startCountDownTimer(time: Long) {
        stopCountDownTimer()
        _timerCountDown.value = time
        timerCountDownJob = viewModelScope.launch {
            while (_timerCountDown.value > 0) {
                delay(1000)
                _timerCountDown.value--
            }
            pauseCountDownTimer()
        }
    }

    fun pauseCountDownTimer() {
        timerCountDownJob?.cancel()
    }

    fun stopCountDownTimer() {
        _timerCountDown.value = -1L
        timerCountDownJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerCountUpJob?.cancel()
        timerCountDownJob?.cancel()
    }

}