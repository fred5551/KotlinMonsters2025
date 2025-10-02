package org.example.monstre
import kotlin.random.Random
class Technique(val id: Int,
                val nom: String,
                val precision: Double,
                val multiplicateurPuissance: Double,
                val estSpecial: Boolean,
                val estBuff: Boolean,
                val estDebuff: Boolean,
                val faireDegats: Boolean,
                val elementTechnique: Element) {
    /**
     * Détermine si une attaque touche ou non la cible.
     * Retourne vrai si le tirage aléatoire <= précision.
     */
    fun calculPrecision(): Boolean {
        val nb = Random.nextInt(1, 101) // valeur entre 1 et 100
        return nb <= precision
    }

    /**
     * Applique le bonus/malus STAB :
     * - Si le monstre possède le même élément que la technique → +15%
     * - Sinon → -15% (avec minimum à 0.1)
     */
    fun calculBonusStab(attaquant: IndividuMonstre): Double {
        val aLesMemeElement = attaquant.espece.elements.contains(elementTechnique)
        return if (aLesMemeElement) {
            multiplicateurPuissance * 1.15
        } else {
            val resultat = multiplicateurPuissance * 0.85
            if (resultat < 0.1) 0.1 else resultat
        }
    }

    /**
     * Applique l’effet de la technique :
     * - inflige des dégâts (si faireDegats = true)
     * - (TODO) appliquer Buffs / Debuffs
     */
    fun effet(attaquant: IndividuMonstre, defenseur: IndividuMonstre): Double {
        if (!faireDegats) return 0.0

        // Base dégâts : dépend de si c’est une attaque spéciale
        val degatsBase = if (estSpecial) attaquant.attaqueSpe else attaquant.attaque

        // Bonus/Malus STAB
        val multiplicateur = calculBonusStab(attaquant)

        // Efficacité élémentaire
        var multiElement = elementTechnique.efficaciteContre(defenseur.espece.elements[0])
        if (defenseur.espece.elements.size > 1) {
            multiElement *= elementTechnique.efficaciteContre(defenseur.espece.elements[1])
        }

        return (degatsBase * multiplicateur) * multiElement
    }
}
