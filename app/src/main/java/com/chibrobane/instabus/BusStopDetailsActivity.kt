package com.chibrobane.instabus

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chibrobane.instabus.adapter.BusStopDetailsAdapter
import com.chibrobane.instabus.data.BusStopDetails
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BusStopDetailsActivity : AppCompatActivity()  {

    private var busStopImages : MutableList<BusStopDetails> = ArrayList()
    private lateinit var adapter : BusStopDetailsAdapter

    private val codeRequestNewImage = 2  // Code qui nous permettra de récupérer la photo prise par l'utilisateur

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_stop_details_activity)

        setSupportActionBar(findViewById(R.id.mytoolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        createFalseData()
        // TODO : Créer une base de données SQL, et stocker les images sur un serveur pour les récupérer ici par la suite

        title = intent.getStringExtra("name_bus_stop")  // On donne le nom de l'arrêt à l'activité

        val recyclerView = findViewById<RecyclerView>(R.id.bus_images_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = BusStopDetailsAdapter(this, busStopImages as MutableList<BusStopDetails>)
        recyclerView.adapter = adapter

        registerForContextMenu(recyclerView)

        val addImageButton : FloatingActionButton = findViewById(R.id.add_bus_image)
        addImageButton.setOnClickListener {
            // Créé une nouvelle activité permettant de prendre une photo et ajouter un titre
            val intent = Intent(this, NewImageActivity::class.java).apply {
                putExtra("parent", title!!)
            }
            startActivityForResult(intent, codeRequestNewImage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.secondary_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // Fonction permettant de vérifier quand un item est sélectionné dans le menu contextuel
        // Soit pour supprimer, soit pour voir une image
        return return when(item.itemId) {
            R.id.view_image -> {
                // TODO : Afficher l'image en grand
                Toast.makeText(this, "Voir l'image", Toast.LENGTH_LONG).show()
                true
            }

            R.id.delete_image -> {
                // TODO : Supprimer l'image de la base de données
                Toast.makeText(this, "Supprimer l'image", Toast.LENGTH_LONG).show()
                true
            }
            else -> return false
        }

    }

    fun createFalseData() {
        // Fonction temporaire afin de créer de fausses données
        // Présente dans le seul but de tester le layout, le temps d'avoir de réelles données
        busStopImages.add(BusStopDetails("Titre 1", null, "06-12-2001"))
        busStopImages.add(BusStopDetails("Titre 2", null, "26-10-2002"))
        busStopImages.add(BusStopDetails("Titre 3", null, "14-02-2012"))
        busStopImages.add(BusStopDetails("Titre 4", null, "15-02-2012"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == codeRequestNewImage && resultCode == Activity.RESULT_OK) {
            // Seulement si on obtient bel et bien une image en retour
            val title : String? = data?.getStringExtra("title")
            val image : Bitmap? = data?.getParcelableExtra("image")

            // TODO : Envoyer l'image et son titre à la base de données (très sûrement mettre l'image en base64 avant, et se moquer de la bienséance)
        }
    }
}