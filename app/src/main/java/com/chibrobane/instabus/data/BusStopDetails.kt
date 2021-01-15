package com.chibrobane.instabus.data

data class BusStopDetails (
        val title: String,
        val image: String,
        val date_of_creation: String,
        val id_image: Int
)

// Classe représentant les informations à envoyer au serveur pour enregistrer une image en BDD
// On envoie seulement l'image et son titre.
data class ImageToPOST (
        val idBusStop: Int,
        val title: String,
        val image: String
)