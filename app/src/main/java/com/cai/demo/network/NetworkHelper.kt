package com.cai.demo.network

import com.cai.demo.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT = 5L
private const val WRITE_TIMEOUT = 5L
private const val READ_TIMEOUT = 5L

object NetworkHelper {
  private val gson = GsonBuilder().create()

  private val loggingInterceptor = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG)
      level = HttpLoggingInterceptor.Level.BODY
  }

  private val okHttpClient = OkHttpClient.Builder().apply {
    // set time outs
    connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
    writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
    readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
    // set retry policy
    retryOnConnectionFailure(true)
    addInterceptor(loggingInterceptor)
  }.build()

  private val retrofit = Retrofit.Builder().apply {
    baseUrl("https://obscure-earth-55790.herokuapp.com")
    addConverterFactory(GsonConverterFactory.create(gson))
    addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  }.client(okHttpClient).build()

  fun createMuseumService(): MuseumService {
    return retrofit.create(MuseumService::class.java)
  }
}