package org.example.monde
import org.example.monstre.EspeceMonstre
import org.example.monstre.IndividuMonstre
import org.example.dresseur.Entraineur
import org.example.jeu.CombatMonstre
import kotlin.random.Random
/**
 * Représente une zone dans le monde du jeu.
 * Une zone peut être une route, une caverne, une mer, etc.
 * On peut y rencontrer des monstres sauvages et se déplacer vers les zones adjacentes.
 *
 * @property id Identifiant unique de la zone.
 * @property nom Nom de la zone.
 * @property expZone Points d'expérience que l'on peut gagner dans cette zone.
 * @property especesMonstres Liste mutable des espèces de monstres pouvant être rencontrées dans cette zone.
 *                          Par défaut, cette liste est vide.
 * @property zoneSuivante Zone suivante accessible depuis cette zone, ou null si aucune.
 * @property zonePrecedente Zone précédente accessible depuis cette zone, ou null si aucune.
 */
class Zone (val id: Int,
            val nom: String,
            val expZone: Int,
            val especesMonstres: MutableList<EspeceMonstre> = mutableListOf(),
            var zoneSuivante: Zone? = null,
            var zonePrecedente: Zone? = null) {
    // TODO: Implémenter la méthode genereMonstre() qui génère un monstre aléatoire dans la zone
    fun genereMonstre(): IndividuMonstre {
        if (especesMonstres.isEmpty()) {
            throw IllegalStateException("Aucune espèce disponible dans la zone $nom")
    }
        val especeChoisie = especesMonstres.random()
        val variation = Random.nextDouble(0.8, 1.2) // +/- 20%
        val expInit = expZone * variation

        return IndividuMonstre(
            id = Random.nextInt(1000, 9999),
            nom = especeChoisie.nom,
            expInit = expInit,
            espece = especeChoisie,
            entraineur = null
        )
    }

    // TODO: Implémenter la méthode rencontreMonstre() qui gère la rencontre avec un monstre
    fun rencontreMonstre(joueur: Entraineur) {
        val monstreSauvage = genereMonstre()
        val premierMonstre = joueur.equipeMonstre.firstOrNull { it.pv > 0 }

        if (premierMonstre == null) {
            println("Tous vos monstres sont K.O. Vous ne pouvez pas combattre.")
            return
        }
        val combat = CombatMonstre(joueur, premierMonstre, monstreSauvage)
        combat.lanceCombat()
    }
}
