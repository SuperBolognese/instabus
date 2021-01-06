package com.chibrobane.instabus.data


data class ResponseDataBus(
    val data: Stations
)

data class Stations (
    val tmbs: List<BusStop>
)

data class BusStop(val id: Int,
              val street_name: String,
              val city: String,
              val utm_x: String,
              val utm_y: String,
              val lat: Double,
              val lon: Double,
              val furniture: String,
              val buses: String
              )