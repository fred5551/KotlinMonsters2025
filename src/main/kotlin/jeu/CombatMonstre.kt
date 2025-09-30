package org.example.jeu
import org.example.monstre.IndividuMonstre
import org.example.dresseur.Entraineur
import org.example.item.Utilisable

class CombatMonstre (val joueur: Entraineur,
                     var monstreJoueur: IndividuMonstre,
                     val monstreSauvage: IndividuMonstre){

    var round: Int = 1
    fun gameOver(): Boolean {
        // Défaite si aucun monstre de l'équipe n'a de PV > 0
        return joueur.equipeMonstre.all { it.pv <= 0 }
    }
    fun joueurGagne(): Boolean {
        // Cas 1 : Monstre sauvage KO
        if (monstreSauvage.pv <= 0) {
            println("${joueur.nom} a gagné !")
            val gainExp = monstreSauvage.exp * 0.20
            monstreJoueur.exp += gainExp
            println("${monstreJoueur.nom} gagne $gainExp exp")
            return true
        }
        // Cas 2 : Monstre capturé
        if (monstreSauvage.entraineur == joueur) {
            println("${monstreSauvage.nom} a été capturé !")
            return true
        }
        // Sinon, combat continue
        return false
    }
    fun actionAdversaire() {
        if (monstreSauvage.pv > 0) {
            monstreSauvage.attaquer(monstreJoueur)
        }
    }
    fun actionJoueur(): Boolean {
        println("Choisissez une action :")
        println("1. Attaquer")
        println("2. Utiliser un objet")
        println("3. Changer de monstre")
        val choix = readLine()?.toIntOrNull()

        when (choix) {
            1 -> {
                monstreJoueur.attaquer(monstreSauvage)
            }
            2 -> {
                if (joueur.sacAItems.isEmpty()) {
                    println("Votre sac est vide !")
                } else {
                    println("Objets disponibles :")
                    joueur.sacAItems.forEachIndexed { index, item ->
                        println("${index + 1}. ${item.nom}")
                    }
                    val choixItem = readLine()?.toIntOrNull()
                    if (choixItem != null && choixItem in 1..joueur.sacAItems.size) {
                        val itemChoisi = joueur.sacAItems[choixItem - 1]
                        if (itemChoisi is Utilisable) {
                            itemChoisi.utiliser(monstreSauvage)
                        } else {
                            println("Cet objet n'est pas utilisable en combat.")
                        }
                    }
                }
            }
            3 -> {
                println("Choisissez un autre monstre de votre équipe :")
                joueur.equipeMonstre.forEachIndexed { index, monstre ->
                    println("${index + 1}. ${monstre.nom} (PV: ${monstre.pv}/${monstre.pvMax})")
                }
                val choixMonstre = readLine()?.toIntOrNull()
                if (choixMonstre != null && choixMonstre in 1..joueur.equipeMonstre.size) {
                    val nouveau = joueur.equipeMonstre[choixMonstre - 1]
                    if (nouveau.pv > 0) {
                        monstreJoueur = nouveau
                        println("Vous envoyez ${nouveau.nom} au combat !")
                    } else {
                        println("Ce monstre est K.O. !")
                    }
                }
            }
            else -> println("Action invalide.")
        }

        // Vérifier si combat doit continuer
        return !(gameOver() || joueurGagne())
    }
    fun afficheCombat() {
        println("======== Début Round : $round ========")
        println("Niveau : ${monstreSauvage.niveau}")
        println("PV : ${monstreSauvage.pv} / ${monstreSauvage.pvMax}")
        println(monstreSauvage.espece.afficheArt()) // ASCII face sauvage
        println(monstreJoueur.espece.afficheArt()) // ASCII dos joueur
        println("Niveau : ${monstreJoueur.niveau}")
        println("PV : ${monstreJoueur.pv} / ${monstreJoueur.pvMax}")
    }
    fun jouer() {
        val joueurPlusRapide = monstreJoueur.vitesse >= monstreSauvage.vitesse
        afficheCombat()

        if (joueurPlusRapide) {
            val continuer = actionJoueur()
            if (!continuer) return
            actionAdversaire()
        } else {
            actionAdversaire()
            if (!gameOver()) {
                val continuer = actionJoueur()
                if (!continuer) return
            }
        }
    }
    fun lanceCombat() {
        while (!gameOver() && !joueurGagne()) {
            this.jouer()
            println("======== Fin du Round : $round ========")
            round++
        }
        if (gameOver()) {
            joueur.equipeMonstre.forEach { it.pv = it.pvMax }
            println("Game Over !")
        }
    }
}
