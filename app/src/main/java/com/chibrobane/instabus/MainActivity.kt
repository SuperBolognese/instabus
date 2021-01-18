
package com.chibrobane.instabus

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
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

    companion object {
        var busStops : List<BusStop> = ArrayList()
    }
    private lateinit var busAdapter : BusStopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.mytoolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //créer une coroutine -> marche pas tant qu'on est pas connecté à l'API
        runBlocking {
            val c = CoroutineScope(Dispatchers.IO).launch {
                callWebService()
            }
            c.join()
        }
        // TODO : Stocker le fichier JSON sur le téléphone afin de l'utiliser pour un mode hors-ligne

        val recyclerView = findViewById<RecyclerView>(R.id.bus_stop_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)//afficher en liste verticale
        busAdapter = BusStopAdapter( this, busStops as MutableList<BusStop>)
        recyclerView.adapter = busAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
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

    fun getStopByName(name: String) : BusStop? {
        // Fonction permettant d'obtenir un arrêt à partir de son nom
        for (busStop in busStops) {
            if (busStop.street_name == name) {
                return busStop
            }
        }
        return null
    }

    fun showBusStop(view : View) {

        // On commence d'abord par récupérer le nom de l'arrêt sur lequel l'utilisateur à cliqué
        val textView : TextView = view.findViewById(R.id.txt_name)
        // On s'en sert ensuite pour récupérer l'objet en lui-même
        val stop = getStopByName(textView.text.toString())

        // On peut maintenant créer une intent afin de créer une activité en lui envoyant les informations requises
        val intent = Intent(this, BusStopDetailsActivity::class.java).apply {
            putExtra("name_bus_stop", stop?.street_name)
            putExtra("id_bus_stop", stop?.id)
        }
        startActivity(intent)
    }

    fun onLaunchMaps(item: MenuItem) {
        val intent = Intent(this, MapsActivity::class.java).apply {
        }
        startActivity(intent)
    }

}
