package org.example.item

import org.example.dresseur.Entraineur

class Badge(id: Int,
            nom: String,
            description: String,
            var champion: Entraineur ) : Item(id, nom, description){
    fun afficheDetailBadge() {
        println("🏅 Badge : $nom")
        println("Description : $description")
        println("Champion associé : ${champion.nom}")
    }
}