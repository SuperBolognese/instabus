package com.chibrobane.instabus

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chibrobane.instabus.adapter.BusStopAdapter
import com.chibrobane.instabus.data.BusStop
import com.chibrobane.instabus.data.BusStopService
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private val url = "http://barcelonaapi.marcpous.com/"

    private var busStops : List<BusStop> = ArrayList<BusStop>()
    private lateinit var busAdapter : BusStopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        runBlocking {
            val c = CoroutineScope(Dispatchers.IO).launch {
                callWebService()
            }
            c.join()
        }

        var recycler_view = findViewById<RecyclerView>(R.id.bus_stop_recycler_view)
        recycler_view.layoutManager = LinearLayoutManager(this)
        busAdapter = BusStopAdapter(recycler_view, this, busStops as MutableList<BusStop>)
        recycler_view.adapter = busAdapter
    }

    @WorkerThread
    fun callWebService() = runBlocking {
        if (isNetworkAvailable()) {
            // Seulement si le réseau est disponible, nous allons télécharger le JSON

            // On crée notre retrofit, afin de pouvoir générer notre requête par la suite
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            // On crée notre Service à partir de Retrofit
            val service = retrofit.create(BusStopService::class.java)
            // On crée notre requête à partir du Service
            val busStopRequest = service.listBusStop()

            val response = busStopRequest.execute().body()
            busStops = response!!.data.tmbs
        }
    }

    @SuppressLint("ServiceCast")
    fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting?:false
    }

}