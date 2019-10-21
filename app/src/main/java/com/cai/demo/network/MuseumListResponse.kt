package com.cai.demo.network

import com.google.gson.annotations.SerializedName


data class MuseumEntity(
  @SerializedName("id") val id: Int,
  @SerializedName("name") val name: String,
  @SerializedName("photo") val img: String
)

data class MuseumResponse(
  @SerializedName("status") val status: Int?,
  @SerializedName("msg") val msg: String?,
  @SerializedName("data") val data: List<MuseumEntity>?
) {
  fun isSuccess(): Boolean = (status == 200)
}