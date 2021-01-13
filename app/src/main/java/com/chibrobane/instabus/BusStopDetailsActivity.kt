package com.chibrobane.instabus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chibrobane.instabus.adapter.BusStopDetailsAdapter
import com.chibrobane.instabus.data.BusStopDetails

class BusStopDetailsActivity : AppCompatActivity()  {

    private var busStopImages : MutableList<BusStopDetails> = ArrayList()
    private lateinit var adapter : BusStopDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_stop_details_activity)

        createFalseData()
        // TODO : Créer un bouton permettant de prendre une nouvelle photo, une base de données SQL, et stocker les images sur un serveur pour les récupérer ici par la suite

        title = intent.getStringExtra("name_bus_stop")
        val recyclerView = findViewById<RecyclerView>(R.id.bus_images_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = BusStopDetailsAdapter(this, busStopImages as MutableList<BusStopDetails>)
        recyclerView.adapter = adapter
    }

    fun createFalseData() {
        // Fonction temporaire afin de créer de fausses données
        // Présente dans le seul but de tester le layout, le temps d'avoir de réelles données
        busStopImages.add(BusStopDetails("Titre 1", null, "06-12-2001"))
        busStopImages.add(BusStopDetails("Titre 2", null, "26-10-2002"))
        busStopImages.add(BusStopDetails("Titre 3", null, "14-02-2012"))
        busStopImages.add(BusStopDetails("Titre 4", null, "15-02-2012"))
    }

}