package com.chibrobane.instabus.database

import com.chibrobane.instabus.data.BusStopDetails
import com.chibrobane.instabus.data.ImageToPOST
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DatabaseService {

    @Headers("Content-Type: application/json")
    @POST("instabusAPI/new_image.php")
    fun newImage(@Body img: ImageToPOST): Call<responseDB>  // Le serveur retourne 0 en cas d'échec ou 1

    @Headers("Content-Type: application/json")
    @POST("instabusAPI/remove_image.php")
    fun removeImage(@Body id: Int): Call<responseDB>

    @Headers("Content-Type: application/json")
    @POST("instabusAPI/get_images.php")
    fun getImages(@Body id: Int): Call<responseDBImages>
}

data class responseDB(
        val returnCode: Int,
        val error: String
)

data class responseDBImages(
        val returnCode: Int,
        val error: String,
        val listImages : List<BusStopDetails>
)