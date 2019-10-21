package com.cai.demo.di

import com.cai.demo.interactor.MuseumInteractor
import com.cai.demo.interactor.MuseumInteractorImpl
import com.cai.demo.network.networkModule
import com.cai.demo.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  viewModel { MainViewModel() }

  factory<MuseumInteractor> { MuseumInteractorImpl(get()) }
} + networkModule