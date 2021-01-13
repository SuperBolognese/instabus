package com.chibrobane.instabus

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class NewImageActivity : AppCompatActivity() {

    private val requestCodeCamera = 1  // Code qui va nous permettre de garder en mémoire notre requête, afin de récupérer la photo prise par l'utilisateur
    lateinit var imageView: ImageView
    private var imageBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_new_image)

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            requestPermissions(arrayOf(Manifest.permission.CAMERA), requestCodeCamera)

        // Lorsqu'on clique sur l'image actuelle, on veut pouvoir en prendre une nouvelle
        imageView = findViewById(R.id.image_bus_stop)
        imageView.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, requestCodeCamera)
        }

        // Lorsqu'on clique sur le bouton envoyer, on retourne l'image
        val buttonSend : MaterialButton = findViewById(R.id.button_send_new_image)
        buttonSend.setOnClickListener {
            // On envoie l'image seulement s'il y en a une
            if (imageBitmap != null) {
                // On récupère le titre
                val textInput : TextInputLayout = findViewById(R.id.title_new_image)

                val returnIntent = Intent()
                returnIntent.putExtra("image", imageBitmap)  // On renvoie l'image
                returnIntent.putExtra("title", textInput.editText?.text)  // Ainsi que son titre
                setResult(RESULT_OK, returnIntent)  // On retourne le résultat de l'activité
                finish()  // Et on la termine
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeCamera && resultCode == RESULT_OK) {
            // Lorsque l'utilisateur prend une photo, on la récupère et on l'affiche à la place de l'ancienne image
            val image: Bitmap = data?.extras?.get("data") as Bitmap
            imageBitmap = image
            imageView.setImageBitmap(image)
        }
    }

}