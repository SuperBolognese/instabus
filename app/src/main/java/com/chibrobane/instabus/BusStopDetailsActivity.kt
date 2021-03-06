package com.chibrobane.instabus

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chibrobane.instabus.adapter.BusStopDetailsAdapter
import com.chibrobane.instabus.data.BusStopDetails
import com.chibrobane.instabus.database.DatabaseConnection
import com.chibrobane.instabus.maps.MapsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream

class BusStopDetailsActivity : AppCompatActivity()  {

    private var busStopImages : MutableList<BusStopDetails> = ArrayList()
    private lateinit var adapter : BusStopDetailsAdapter
    private var id_stop : Int = 0

    private val codeRequestNewImage = 2  // Code qui nous permettra de récupérer la photo prise par l'utilisateur
    private val db : DatabaseConnection = DatabaseConnection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_stop_details_activity)

        db.setContext(applicationContext)

        setSupportActionBar(findViewById(R.id.mytoolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = intent.getStringExtra("name_bus_stop")  // On donne le nom de l'arrêt à l'activité
        id_stop = intent.getIntExtra("id_bus_stop", -1)

        // On récupère les images actuellement sur le serveur
        //busStopImages = db.getImagesOf(id_stop)!!

        val recyclerView = findViewById<RecyclerView>(R.id.bus_images_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = BusStopDetailsAdapter(this, busStopImages)
        recyclerView.adapter = adapter

        registerForContextMenu(recyclerView)

        db.getImagesOf(id_stop, recyclerView, this)

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
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // Fonction permettant de vérifier quand un item est sélectionné dans le menu contextuel
        // Soit pour supprimer, soit pour voir une image

        return return when(item.order) {
            1 -> {
                // TODO : Supprimer l'image de la base de données
                db.removeImage(item.itemId)
                Toast.makeText(this, "Supprimer l'image", Toast.LENGTH_LONG).show()
                true
            }
            else -> return false
        }

    }

    fun encodeImage(image: Bitmap?) : String {
        // Fonction qui transforme une image en Base64 pour l'enregistrer dans la BDD
        if (image == null)
            return ""

        // Nous sommes maintenant sûrs que l'image n'est pas nulle
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val bytes = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == codeRequestNewImage && resultCode == Activity.RESULT_OK) {
            // Seulement si on obtient bel et bien une image en retour
            val title : String? = data?.getStringExtra("title")
            val image : Bitmap? = data?.getParcelableExtra("image")

            // TODO : Envoyer l'image et son titre à la base de données (très sûrement mettre l'image en base64 avant, et se moquer de la bienséance)
            db.addImageTo(id_stop, title!!, encodeImage(image))
        }
    }

    fun onLaunchMaps(item: MenuItem) {
        val intent = Intent(this, MapsActivity::class.java).apply {
        }
        startActivity(intent)
    }

    fun launchMainAct(item: MenuItem){
        val intent = Intent(this, MainActivity::class.java).apply{
        }
        startActivity(intent)
    }
}