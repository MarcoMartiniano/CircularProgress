package com.example.myapplication.di

import com.example.myapplication.presentation.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainScreenViewModel() }
}