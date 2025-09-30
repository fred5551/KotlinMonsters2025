package org.example.jeu

import org.example.dresseur.Entraineur
import org.example.especeAquamy
import org.example.especeFlamkip
import org.example.especeSpringleaf
import org.example.monde.Zone
import org.example.monstre.IndividuMonstre
import java.util.*
class Partie(val id: Int,
             val joueur: Entraineur,
             var zone: Zone) {
    fun choixStarter() {
        val monstre1 = IndividuMonstre(1, "Springleaf", 0.0, especeSpringleaf, joueur)
        val monstre2 = IndividuMonstre(2, "Flamkip", 0.0, especeFlamkip, joueur)
        val monstre3 = IndividuMonstre(3, "Aquamy", 0.0, especeAquamy, joueur)

        println("=== Choisissez votre starter ===")
        println("1 - ${monstre1.nom}")
        println("2 - ${monstre2.nom}")
        println("3 - ${monstre3.nom}")

        val choix = readLine()?.toIntOrNull() ?: 1
        val starter = when (choix) {
            1 -> monstre1
            2 -> monstre2
            else -> monstre3
        }

        starter.renommer()
        joueur.equipeMonstre.add(starter)
        starter.entraineur = joueur
    }

    fun modifierOrdreEquipe() {
        if (joueur.equipeMonstre.size < 2) {
            println("Vous n'avez pas assez de monstres pour modifier l'ordre.")
            return
        }

        println("=== Ordre actuel ===")
        joueur.equipeMonstre.forEachIndexed { i, monstre ->
            println("${i+1} - ${monstre.nom}")
        }

        println("Sélectionnez le monstre à déplacer : ")
        val pos1 = (readLine()?.toIntOrNull() ?: return) - 1
        println("Nouvelle position : ")
        val pos2 = (readLine()?.toIntOrNull() ?: return) - 1

        if (pos1 !in joueur.equipeMonstre.indices || pos2 !in joueur.equipeMonstre.indices) {
            println("Positions invalides.")
            return
        }

        Collections.swap(joueur.equipeMonstre, pos1, pos2)
        println("Ordre modifié avec succès.")
    }

    fun examineEquipe() {
        var continuer = true
        while (continuer) {
            println("=== Votre équipe ===")
            joueur.equipeMonstre.forEachIndexed { i, monstre ->
                println("${i+1} - ${monstre.nom}")
            }
            println("Tapez un numéro pour voir les détails, m pour modifier, q pour quitter")

            when (val choix = readLine()) {
                "q" -> continuer = false
                "m" -> modifierOrdreEquipe()
                else -> {
                    val index = choix?.toIntOrNull()
                    if (index != null && index-1 in joueur.equipeMonstre.indices) {
                        joueur.equipeMonstre[index-1].afficheDetail()
                    }
                }
            }
        }
    }

    fun jouer() {
        var continuer = true
        while (continuer) {
            println("Vous êtes dans la zone : ${zone.nom}")
            println("1 - Rencontrer un monstre sauvage")
            println("2 - Examiner l’équipe")
            println("3 - Aller à la zone suivante")
            println("4 - Aller à la zone précédente")
            println("q - Quitter la partie")

            when (readLine()) {
                "1" -> zone.rencontreMonstre(joueur)
                "2" -> examineEquipe()
                "3" -> if (zone.zoneSuivante != null) {
                    zone = zone.zoneSuivante!!
                    println("Vous avancez vers ${zone.nom}")
                }
                "4" -> if (zone.zonePrecedente != null) {
                    zone = zone.zonePrecedente!!
                    println("Vous revenez vers ${zone.nom}")
                }
                "q" -> continuer = false
            }
        }
    }

}