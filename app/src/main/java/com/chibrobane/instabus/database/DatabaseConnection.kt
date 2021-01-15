package com.chibrobane.instabus.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chibrobane.instabus.adapter.BusStopDetailsAdapter
import com.chibrobane.instabus.data.BusStopDetails
import com.chibrobane.instabus.data.ImageToPOST
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.logging.Handler

object DatabaseConnection {

    private lateinit var context: Context

    private const val url : String = "http://192.168.1.70/"  // Adresse de mon serveur, détenant une API pour communiquer avec la BDD
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create()).client(client).build()
    private val service = retrofit.create(DatabaseService::class.java)

    fun setContext(context: Context) {
        this.context = context
    }

    fun getImagesOf(id: Int?, recyclerView: RecyclerView, activity: AppCompatActivity) {
        // Fonction téléchargeant sur le serveur les images de la communauté
        // (En base64)

        CoroutineScope(Dispatchers.IO).launch {
            val getImagesRequest = service.getImages(id!!)

            getImagesRequest.enqueue(
                object : Callback<responseDBImages> {
                    override fun onResponse(call: Call<responseDBImages>, response: Response<responseDBImages>) {
                        Log.i("DBRESPONSE", response.message())
                        val adapter = BusStopDetailsAdapter(activity, response.body()!!.listImages as MutableList<BusStopDetails>)
                        recyclerView.adapter = adapter
                    }

                    override fun onFailure(call: Call<responseDBImages>, t: Throwable) {
                        Toast.makeText(context, "Une erreur est survenue lors de la récupérations des images.", Toast.LENGTH_LONG).show()
                        Log.i("DBERROR", t.message!!)
                    }
                }
            )
        }
    }

    fun addImageTo(id: Int, title: String, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val imgInfos = ImageToPOST(id, title, image)
            val addImageRequest = service.newImage(imgInfos)

            addImageRequest.enqueue(
                object : Callback<responseDB> {
                    override fun onFailure(call: Call<responseDB>, t: Throwable) {
                        Toast.makeText(context, "Une erreur est survenue lors de l'envoie de l'image.", Toast.LENGTH_LONG).show()
                        Log.i("DBERROR", t.message!!)
                    }

                    override fun onResponse(call: Call<responseDB>, response: Response<responseDB>) {
                        Toast.makeText(context, "L'image a été envoyé, et est en cours de traitement.", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }

    fun removeImage(idImage : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val removeImageRequest = service.removeImage(idImage)

            removeImageRequest.enqueue(
                object : Callback<responseDB> {
                    override fun onResponse(call: Call<responseDB>, response: Response<responseDB>) {
                        Toast.makeText(context, "L'image est en cours de suppression.", Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(call: Call<responseDB>, t: Throwable) {
                        Toast.makeText(context, "Une erreur est survenue lors de la suppression de l'image.", Toast.LENGTH_LONG).show()
                    }

                }
            )
        }
    }
}