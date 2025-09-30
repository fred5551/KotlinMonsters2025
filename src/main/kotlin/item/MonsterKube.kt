package org.example.item
import org.example.monstre.IndividuMonstre
class MonsterKube(id: Int,
                  nom: String,
                  description: String,
                  var chanceCapture: Double) : Item(id, nom, description), Utilisable {
    override fun utiliser(cible: IndividuMonstre): Boolean {
        println("Vous lancez le Monster Kube !")

        if (cible.entraineur != null) {
            println("Le monstre ne peut pas être capturé.")
            return false
        }

        val ratioVie = cible.pv.toDouble() / cible.pvMax.toDouble()
        var chanceEffective = chanceCapture * (1.5 - ratioVie)
        chanceEffective = chanceEffective.coerceAtLeast(5.0) // minimum 5%

        val nbAleatoire = (0..100).random()

        return if (nbAleatoire < chanceEffective) {
            println("Le monstre est capturé !")
            // logique d'ajout à l'équipe ou à la boîte ici
            true
        } else {
            println("Presque ! Le Kube n'a pas pu capturer le monstre !")
            false
        }
    }
}