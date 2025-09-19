package org.example.monstre
import org.example.dresseur.Entraineur
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

class IndividuMonstre (val id: Int,
                       var nom: String,
                       expInit: Double,
                       val espece: EspeceMonstre,
                       var entraineur: Entraineur? = null){
 var niveau: Int = 1
var attaque: Int
var defense: Int
var vitesse: Int
var attaqueSpe: Int
var defenseSpe: Int
var pvMax: Int
val potentiel: Double = Random.nextDouble(0.5, 2.0)

// exp avec getter/setter + level up dans setter
var exp: Double = 0.0
    set(value) {
        field = if (value < 0) 0.0 else value
        while (field >= palierExp(niveau + 1)) {
            levelUp()
        }
    }

// pv avec getter/setter qui clamp entre 0 et pvMax
var pv: Int = 0
    get() = field
    set(nouveauPv) {
        field = when {
            nouveauPv < 0 -> 0
            nouveauPv > pvMax -> pvMax
            else -> nouveauPv
        }
    }

init {
    // Initialiser stats avec la base + ou - aléatoire (entre -2 et +2), sauf pvMax -5/+5
    attaque = espece.baseAttaque + Random.nextInt(-2, 3)
    defense = espece.baseDefense + Random.nextInt(-2, 3)
    vitesse = espece.baseVitesse + Random.nextInt(-2, 3)
    attaqueSpe = espece.baseAttaqueSpe + Random.nextInt(-2, 3)
    defenseSpe = espece.baseDefenseSpe + Random.nextInt(-2, 3)
    pvMax = espece.basePv + Random.nextInt(-5, 6)
    if (pvMax < 1) pvMax = 1 // éviter pvMax négatif ou nul
    pv = pvMax
    this.exp = expInit // déclenche setter et level up éventuel
}

/**
 * Calcule l'expérience totale nécessaire pour atteindre un niveau donné.
 * @param niveau Niveau cible.
 * @return Expérience cumulée nécessaire pour atteindre ce niveau.
 */
fun palierExp(niveau: Int): Double {
    return 100 * (niveau - 1).toDouble().pow(2.0)
}

/**
 * Augmente le niveau du monstre, recalcul les caractéristiques et augmente les PV en conséquence.
 */
fun levelUp() {
    niveau++
    println("Le monstre $nom est maintenant niveau $niveau !")

    fun calcStat(baseStat: Int, modCarac: Double, potentiel: Double, randomRange: IntRange): Int {
        val statBonus = (modCarac * potentiel).roundToInt() + Random.nextInt(randomRange.first, randomRange.last + 1)
        return baseStat + statBonus
    }

    attaque = calcStat(attaque, espece.modAttaque, potentiel, -2..2)
    defense = calcStat(defense, espece.modDefense, potentiel, -2..2)
    vitesse = calcStat(vitesse, espece.modVitesse, potentiel, -2..2)
    attaqueSpe = calcStat(attaqueSpe, espece.modAttaqueSpe, potentiel, -2..2)
    defenseSpe = calcStat(defenseSpe, espece.modDefenseSpe, potentiel, -2..2)

    val ancienPvMax = pvMax
    pvMax = calcStat(pvMax, espece.modPv, potentiel, -5..5)
    if (pvMax < 1) pvMax = 1

    // Augmentation des PV actuels du monstre en fonction de la différence de pvMax
    pv += (pvMax - ancienPvMax)
    if (pv > pvMax) pv = pvMax
}

/**
 * Attaque un autre individu monstre et inflige des dégâts.
 *
 * Dégâts calculés par : attaque - (défense / 2), minimum 1 dégât.
 * @param cible Monstre cible.
 */
fun attaquer(cible: IndividuMonstre) {
    val degatBrut = this.attaque
    var degatTotal = degatBrut - (cible.defense / 2)
    if (degatTotal < 1) degatTotal = 1

    val pvAvant = cible.pv
    cible.pv -= degatTotal
    val pvApres = cible.pv

    println("$nom inflige ${pvAvant - pvApres} dégâts à ${cible.nom}")
}

/**
 * Demande au joueur de renommer le monstre.
 * Si l'utilisateur entre un texte vide, le nom n'est pas modifié.
 */
fun renommer() {
    println("Renommer $nom ? (laisser vide pour ne pas modifier)")
    val nouveauNom = readLine()
    if (!nouveauNom.isNullOrBlank()) {
        nom = nouveauNom
        println("Monstre renommé en $nom")
    }
}

/**
 * Affiche les détails du monstre ainsi que son ASCII art.
 */
fun afficheDetail() {
    val art = espece.afficheArt()
    val artLines = art.lines()

    val details = listOf(
        "Nom : $nom",
        "Niveau : $niveau",
        "PV : $pv / $pvMax",
        "Attaque : $attaque",
        "Défense : $defense",
        "Vitesse : $vitesse",
        "Attaque Spéciale : $attaqueSpe",
        "Défense Spéciale : $defenseSpe",
        "Potentiel : ${"%.2f".format(potentiel)}",
        "Expérience : ${"%.2f".format(exp)}"
    )

    val maxArtWidth = artLines.maxOfOrNull { it.length } ?: 0
    val maxLines = maxOf(artLines.size, details.size)

    for (i in 0 until maxLines) {
        val artLine = if (i < artLines.size) artLines[i] else ""
        val detailLine = if (i < details.size) details[i] else ""
        println(artLine.padEnd(maxArtWidth + 4) + detailLine)
    }
}
}