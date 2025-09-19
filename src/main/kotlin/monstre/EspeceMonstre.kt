package org.example.monstre

import java.io.File

/**
 * Classe représentant une espèce de monstre.
 * Cette classe stocke les caractéristiques communes à tous les monstres d'une même espèce.
 *
 * @property id Identifiant unique de l’espèce.
 * @property nom Nom de l’espèce.
 * @property type Type de l’espèce (ex : Animal, Graine, etc.).
 * @property baseAttaque Statistique d’attaque de base.
 * @property baseDefense Statistique de défense de base.
 * @property baseVitesse Statistique de vitesse de base.
 * @property baseAttaqueSpe Statistique d’attaque spéciale de base.
 * @property baseDefenseSpe Statistique de défense spéciale de base.
 * @property basePv Points de vie de base.
 * @property modAttaque Modificateur d’évolution pour l’attaque.
 * @property modDefense Modificateur d’évolution pour la défense.
 * @property modVitesse Modificateur d’évolution pour la vitesse.
 * @property modAttaqueSpe Modificateur d’évolution pour l’attaque spéciale.
 * @property modDefenseSpe Modificateur d’évolution pour la défense spéciale.
 * @property modPv Modificateur d’évolution pour les PV.
 * @property description Description textuelle de l’espèce.
 * @property particularites Particularités notables de l’espèce.
 * @property caractères Traits de caractère généralement associés à l’espèce.
 */
class EspeceMonstre(
    var id: Int,
    var nom: String,
    var type: String,
    val baseAttaque: Int,
    val baseDefense: Int,
    val baseVitesse: Int,
    val baseAttaqueSpe: Int,
    val baseDefenseSpe: Int,
    val basePv: Int,
    val modAttaque: Double,
    val modDefense: Double,
    val modVitesse: Double,
    val modAttaqueSpe: Double,
    val modDefenseSpe: Double,
    val modPv: Double,
    val description: String = "",
    val particularites: String = "",
    val caractères: String = ""
) {
    /**
     * Affiche la représentation artistique ASCII du monstre.
     *
     * @param deFace Détermine si l'art affiché est de face (true) ou de dos (false).
     *               La valeur par défaut est true.
     * @return Une chaîne de caractères contenant l'art ASCII du monstre avec les codes couleur ANSI.
     *         L'art est lu à partir d'un fichier texte dans le dossier resources/art.
     */
    fun afficheArt(deFace: Boolean = true): String {
        val nomFichier = if (deFace) "front" else "back"
        val chemin = "src/main/resources/art/${this.nom.lowercase()}/$nomFichier.txt"

        return try {
            val art = File(chemin).readText()
            val safeArt = art.replace("/", "∕")
            safeArt.replace("\\u001B", "\u001B")
        } catch (e: Exception) {
            "Erreur : ASCII art non trouvé pour ${this.nom} ($chemin)"
        }
    }
}


