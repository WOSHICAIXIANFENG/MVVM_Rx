package com.cai.demo.network

import org.koin.dsl.module

val networkModule = module {
  single<MuseumService> { NetworkHelper.createMuseumService() }
}