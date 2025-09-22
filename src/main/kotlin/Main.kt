package org.example
import org.example.monstre.EspeceMonstre
import org.example.dresseur.Entraineur
import org.example.monde.Zone
import org.example.monstre.IndividuMonstre
import org.example.item.Badge


var joueur = Entraineur(1,"Sacha",100)
val especeSpringleaf = EspeceMonstre(
    id = 1,
    nom = "Springleaf",
    type = "Graine",
    baseAttaque = 60,
    baseDefense = 9,
    baseVitesse = 11,
    baseAttaqueSpe = 10,
    baseDefenseSpe = 12,
    basePv = 14,
    modAttaque = 34.0,
    modDefense = 6.5,
    modVitesse = 9.0,
    modAttaqueSpe = 8.0,
    modDefenseSpe = 7.0,
    modPv = 10.0,
    description = "Petit monstre espiègle rond comme une graine, adore le soleil.",
    particularites = "Sa feuille sur la tête indique son humeur.",
    caractères = "Curieux, amical, timide"
)

val especeFlamkip = EspeceMonstre(
    id = 4,
    nom = "Flamkip",
    type = "Animal",
    baseAttaque = 50,
    baseDefense = 12,
    baseVitesse = 8,
    baseAttaqueSpe = 13,
    baseDefenseSpe = 16,
    basePv = 7,
    modAttaque = 22.0,
    modDefense = 10.0,
    modVitesse = 5.5,
    modAttaqueSpe = 9.5,
    modDefenseSpe = 9.5,
    modPv = 6.5,
    description = "Petit animal entouré de flammes, déteste le froid.",
    particularites = "Sa flamme change d’intensité selon son énergie.",
    caractères = "Impulsif, joueur, loyal"
)

val especeAquamy = EspeceMonstre(
    id = 7,
    nom = "Aquamy",
    type = "Meteo",
    baseAttaque = 55,
    baseDefense = 10,
    baseVitesse = 11,
    baseAttaqueSpe = 9,
    baseDefenseSpe = 14,
    basePv = 14,
    modAttaque = 27.0,
    modDefense = 9.0,
    modVitesse = 10.0,
    modAttaqueSpe = 7.5,
    modDefenseSpe = 12.0,
    modPv = 12.0,
    description = "Créature vaporeuse semblable à un nuage, produit des gouttes pures.",
    particularites = "Fait baisser la température en s’endormant.",
    caractères = "Calme, rêveur, mystérieux"
)

val route1 = Zone(
    id = 1,
    nom = "Route 1",
    expZone = 20,
    especesMonstres = mutableListOf(especeSpringleaf, especeFlamkip)
)

val route2 = Zone(
    id = 2,
    nom = "Route 2",
    expZone = 25,
    especesMonstres = mutableListOf(especeAquamy)
)

val caverne = Zone(
    id = 3,
    nom = "Caverne Sombre",
    expZone = 30,
    especesMonstres = mutableListOf(especeFlamkip, especeAquamy)
)
var rival = Entraineur(2, "Regis", 200)

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/**
 * Change la couleur du message donné selon le nom de la couleur spécifié.
 * Cette fonction utilise les codes d'échappement ANSI pour appliquer une couleur à la sortie console. Si un nom de couleur
 * non reconnu ou une chaîne vide est fourni, aucune couleur n'est appliquée.
 *
 * @param message Le message auquel la couleur sera appliquée.
 * @param couleur Le nom de la couleur à appliquer (ex: "rouge", "vert", "bleu"). Par défaut c'est une chaîne vide, ce qui n'applique aucune couleur.
 * @return Le message coloré sous forme de chaîne, ou le même message si aucune couleur n'est appliquée.
 */

fun changeCouleur(texte: String, couleur: String): String {
    val reset = "\u001B[0m"
    val codeCouleur = when (couleur.lowercase()) {
        "rouge" -> "\u001B[31m"
        "vert" -> "\u001B[32m"
        "jaune" -> "\u001B[33m"
        "bleu" -> "\u001B[34m"
        "magenta" -> "\u001B[35m"
        "cyan" -> "\u001B[36m"
        "blanc" -> "\u001B[37m"
        "marron" -> "\u001B[38;5;94m"
        else -> ""
    }

    return if (codeCouleur == "") texte else "$codeCouleur$texte$reset"
}

fun main() {
    //println(changeCouleur("Hello","rouge"))
    //println(changeCouleur("World","bleu"))
    //println("Hello ${changeCouleur("my","jaune")} World")
    //println(changeCouleur("Truc","marron"))

    //joueur.afficheDetail()
    //rival.afficheDetail()
    //joueur.argents+=50
    //joueur.afficheDetail()

        println("=== SPRINGLEAF FRONT ===")
        println(especeSpringleaf.afficheArt(deFace = true))
        println("=== SPRINGLEAF BACK ===")
        println(especeSpringleaf.afficheArt(deFace = false))

        println("=== FLAMKIP FRONT ===")
        println(especeFlamkip.afficheArt(deFace = true))
        println("=== FLAMKIP BACK ===")
        println(especeFlamkip.afficheArt(deFace = false))

        println("=== AQUAMY FRONT ===")
        println(especeAquamy.afficheArt(deFace = true))
        println("=== AQUAMY BACK ===")
        println(especeAquamy.afficheArt(deFace = false))


    val monstre1 = IndividuMonstre(1, "springleaf", 1500.0, especeSpringleaf)
    val monstre2 = IndividuMonstre(2, "flamkip", 1500.0, especeFlamkip)
    val monstre3 = IndividuMonstre(3, "aquamy", 1500.0, especeAquamy)

    // === TEST 1 : Vérifier les niveaux après création ===
    println("=== Détails initiaux ===")
    monstre1.afficheDetail()
    monstre2.afficheDetail()
    monstre3.afficheDetail()

    // === TEST 2 : Expérience et Level Up ===
    println("\n=== Test Level Up ===")
    monstre1.exp += 1000.0 // ajoute de l’exp pour déclencher un level-up
    monstre1.afficheDetail()

    // === TEST 3 : PV clamp ===
    println("\n=== Test PV Clamp ===")
    monstre2.pv -= 999  // devrait rester >= 0
    println("PV de ${monstre2.nom} après dégâts massifs : ${monstre2.pv}")
    monstre2.pv += 999  // devrait rester <= pvMax
    println("PV de ${monstre2.nom} après soin excessif : ${monstre2.pv}/${monstre2.pvMax}")

    // === TEST 4 : Attaquer ===
    println("\n=== Test Attaque ===")
    monstre1.attaquer(monstre3)
    println("PV de ${monstre3.nom} après attaque : ${monstre3.pv}/${monstre3.pvMax}")

    // === TEST 5 : Renommer ===
    println("\n=== Test Renommer ===")
   // monstre1.renommer() // demande à l’utilisateur un nouveau nom (laisser vide pour tester)

    // === TEST 6 : Ajout à un entraîneur ===
    println("\n=== Test Entraineur ===")
    joueur.equipeMonstre.add(monstre1)
    joueur.equipeMonstre.add(monstre2)
    joueur.boiteMonstre.add(monstre3)
    joueur.afficheDetail()

    val championPierre = Entraineur(1, "Pierre", 1000)

    val badgeRoche = Badge(
        id = 1,
        nom = "Badge Roche",
        description = "Badge gagné lorsque le joueur atteint l'arène de pierre.",
        champion = championPierre
    )

    // Test d'affichage
    badgeRoche.afficheDetailBadge()

    fun main() {
        val championPierre = Entraineur(1, "Pierre", 1000)
        val monstre = IndividuMonstre(1, "Rocabot", 0.0, EspeceMonstre(...)) // à adapter avec ton constructeur EspeceMonstre

        val potion = Potion(1, "Potion", "Restaure 20 PV", 20)
        potion.utiliser(monstre)
    }

}

