package com.cai.demo.network

import io.reactivex.Single
import retrofit2.http.GET

interface MuseumService {
  @GET("/api/museums/")
  fun museums(): Single<MuseumResponse>
}