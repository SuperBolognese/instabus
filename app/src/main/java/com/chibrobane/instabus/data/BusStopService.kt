package com.chibrobane.instabus.data

import retrofit2.Call
import retrofit2.http.GET

interface BusStopService {
    @GET("/bus/stations.json")
    fun listBusStop(): Call<ResponseDataBus>
}