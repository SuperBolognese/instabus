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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chibrobane.instabus.adapter.BusStopDetailsAdapter
import com.chibrobane.instabus.data.BusStopDetails
import com.chibrobane.instabus.database.DatabaseConnection
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

<<<<<<< HEAD
        db.setContext(applicationContext)
=======
        setSupportActionBar(findViewById(R.id.mytoolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        createFalseData()
        // TODO : Créer une base de données SQL, et stocker les images sur un serveur pour les récupérer ici par la suite
>>>>>>> 4423be64facf42c9a72a74b9efdae5a0a04dc702

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
<<<<<<< HEAD
=======
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.secondary_menu, menu)
        return super.onCreateOptionsMenu(menu)
>>>>>>> 4423be64facf42c9a72a74b9efdae5a0a04dc702
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
        //busStopImages.add(BusStopDetails("Titre 1", null, "06-12-2001"))
        //busStopImages.add(BusStopDetails("Titre 2", null, "26-10-2002"))
        //busStopImages.add(BusStopDetails("Titre 3", null, "14-02-2012"))
        //busStopImages.add(BusStopDetails("Titre 4", null, "15-02-2012"))
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
}