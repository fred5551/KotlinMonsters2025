# Rapport de Projet - KotlinMonsters

**Auteur :** Frédéric BITSINDOU  
**Date :** 12/10/25  
**Classe :** BTS SIO 22  
**Paradigme :** Programmation Orientée Objet

---

## Table des matières

1. [Introduction](#1-introduction)
2. [Architecture du Projet](#2-architecture-du-projet)
3. [Analyse Détaillée des Composants](#3-analyse-détaillée-des-composants)
4. [Concepts de POO Implémentés](#4-concepts-de-poo-implémentés)
5. [Systèmes de Jeu](#5-systèmes-de-jeu)
6. [Exemples de Code Remarquables](#6-exemples-de-code-remarquables)
7. [Tests et Validation](#7-tests-et-validation)
8. [Conclusion](#8-conclusion)

---

## 1. Introduction

Ce projet implémente un système de jeu de capture et de combat de monstres inspiré de la franchise Pokémon. Développé en Kotlin, il met en œuvre les principes fondamentaux de la programmation orientée objet tout en offrant une expérience de jeu complète et interactive.

### 1.1 Objectifs du Projet

- Créer une architecture modulaire et extensible
- Implémenter des mécaniques de jeu RPG (progression, combat, capture)
- Utiliser des concepts avancés de POO (héritage, polymorphisme, encapsulation)
- Développer une interface utilisateur en ligne de commande riche et colorée

### 1.2 Fonctionnalités Principales

- Système de combat tour par tour avec calculs de dégâts complexes
- Gestion d'équipe et d'inventaire
- Progression par expérience et montée de niveaux
- Système de types élémentaires avec forces et faiblesses
- Capture de monstres sauvages
- Exploration de zones interconnectées

---

### 2.1 Diagramme de Classes Simplifié

```
Item (abstract)
  ├── Badge
  └── MonsterKube (implements Utilisable)

Entraineur
  ├── equipeMonstre: List<IndividuMonstre>
  ├── boiteMonstre: List<IndividuMonstre>
  └── sacAItems: List<Item>

EspeceMonstre
  └── IndividuMonstre
      └── techniques: List<Technique>

Zone
  └── especesMonstres: List<EspeceMonstre>

Element
  ├── forces: List<Element>
  └── faiblesses: List<Element>
```

---

## 3. Analyse Détaillée des Composants

### 3.1 Package `dresseur`

#### Classe `Entraineur`

Cette classe représente un joueur ou un adversaire dans le jeu. Elle gère l'équipe active, le stockage des monstres capturés et l'inventaire d'objets.

```kotlin
class Entraineur (
    var id: Int,
    var nom: String,
    var argents: Int,
    var equipeMonstre: MutableList<IndividuMonstre> = mutableListOf(),
    var boiteMonstre: MutableList<IndividuMonstre> = mutableListOf(),
    var sacAItems: MutableList<Item> = mutableListOf()
)
```

Cette classe utilise des valeurs par défaut pour initialiser les collections, évitant ainsi les null pointer exceptions :

```kotlin
var equipeMonstre: MutableList<IndividuMonstre> = mutableListOf(),
var boiteMonstre: MutableList<IndividuMonstre> = mutableListOf(),
var sacAItems: MutableList<Item> = mutableListOf()
```

**Points forts de l'implémentation :**

- Utilisation de valeurs par défaut pour les listes mutables
- Séparation logique entre équipe active et stockage
- Gestion polymorphe des items via la classe abstraite `Item`

**Méthode d'affichage :**

```kotlin
fun afficheDetail(){
    println("Dresseur : ${this.nom}")
    println("Argents: ${this.argents} ")
    println("Équipe (${equipeMonstre.size}) : " + equipeMonstre.joinToString { it.nom })
    println("Boîte (${boiteMonstre.size}) : " + boiteMonstre.joinToString { it.nom })
}
```

Cette méthode utilise efficacement `joinToString` avec une lambda pour afficher les noms des monstres de manière concise.

Méthode `afficheDetail()` - Utilisation de lambdas :

```kotlin
println("Équipe (${equipeMonstre.size}) : " + equipeMonstre.joinToString { it.nom })
println("Boîte (${boiteMonstre.size}) : " + boiteMonstre.joinToString { it.nom })
```

Points techniques :

- `joinToString { it.nom }` : Lambda concise pour extraire les noms
- String interpolation avec `${}` pour intégrer la taille
- Une seule ligne pour afficher une collection complète

---

### 3.2 Package `monstre`

#### Classe `EspeceMonstre`

Représente le "template" d'une espèce avec ses caractéristiques de base et ses modificateurs de croissance.

```kotlin
class EspeceMonstre(
    var id: Int,
    var nom: String,
    var type: String,
    val baseAttaque: Int, // Immuable (val)
    val baseDefense: Int, // Immuable (val)
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
    val caractères: String = "",
    val elements: MutableList<Element> = mutableListOf(), // Liste mutable
    var paliersTechniques: MutableList<PalierTechnique> = mutableListOf() //Variable et  mutable
)
```

**Analyse :**

- Distinction claire entre stats de base (immuables avec `val`) et éléments/paliers (mutables)
- Paramètres optionnels avec valeurs par défaut pour flexibilité
- Séparation des modificateurs permettant une croissance personnalisée par statistique

**Méthode remarquable - Affichage d'art ASCII :**

```kotlin
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
```

**Points remarquables :**

- Interpolation de string dans le chemin : `${this.nom.lowercase()}`
- Bloc `try-catch` pour robustesse
- Remplacement de caractères pour compatibilité : `replace("/", "∕")`
- Traitement des codes ANSI : `replace("\\u001B", "\u001B")`

---

#### Classe `IndividuMonstre`

C'est le cœur du système de jeu. Cette classe implémente la logique individuelle de chaque monstre.

**Initialisation avec variation aléatoire :**

```kotlin
init {
    attaque = espece.baseAttaque + Random.nextInt(-2, 3)
    // ... autres stats ...
    pvMax = espece.basePv + Random.nextInt(-5, 6)
    if (pvMax < 1) pvMax = 1  // Validation pour éviter les PV invalides
    pv = pvMax
    this.exp = expInit  // Déclenche le setter qui gère le level-up
}
```

**Analyse :**

- `Random.nextInt(-2, 3)`: Génère un nombre entre -2 et 2 inclus
- Validation immédiate avec `if (pvMax < 1)`
- `this.exp = expInit` utilise le setter personnalisé

**Propriété calculée avec setter personnalisé :**

```kotlin
var exp: Double = 0.0
    set(value) {
        field = if (value < 0) 0.0 else value
        while (field >= palierExp(niveau + 1)) {
            levelUp()
        }
    }
```

**Analyse approfondie :**

- `field` : Référence au champ backing de la propriété
- `if (value < 0) 0.0 else value` : Validation en une ligne (expression ternaire)
- `while` plutôt que `if` : Gère plusieurs montées de niveau simultanées
- Avantage majeur : Impossible d'oublier de vérifier le level-up, c'est automatique !

**Système de clamping des PV :**

```kotlin
var pv: Int = 0
    get() = field
    set(nouveauPv) {
        field = when {
            nouveauPv < 0 -> 0
            nouveauPv > pvMax -> pvMax
            else -> nouveauPv
        }
    }
```

**Points techniques :**

- `when { }` sans argument : Évalue des conditions booléennes
- Trois branches pour couvrir tous les cas
- Garantit mathématiquement : `0 ≤ pv ≤ pvMax`

**Méthode de montée de niveau :**

Méthode `levelUp()` - Fonction locale

Fonction imbriquée pour éviter la duplication :

```kotlin
ffun levelUp() {
    niveau++

    fun calcStat(baseStat: Int, modCarac: Double, potentiel: Double, randomRange: IntRange): Int {
        val statBonus = (modCarac * potentiel).roundToInt() +
                        Random.nextInt(randomRange.first, randomRange.last + 1)
        return baseStat + statBonus
    }

    attaque = calcStat(attaque, espece.modAttaque, potentiel, -2..2)
    defense = calcStat(defense, espece.modDefense, potentiel, -2..2)
    // ...
}
```

**Analyse approfondie :**

- `fun calcStat()` : Fonction locale, visible uniquement dans levelUp()
- `(modCarac \* potentiel).roundToInt()` : Conversion Double → Int avec arrondi
- `randomRange.first` et `randomRange.last` : Accès aux bornes d'une range
- Avantage : Code DRY (Don't Repeat Yourself) - une seule définition pour 6 statss

##### Méthode `apprendreTechnique()` - Gestion d'interaction

**Validation préalable :**

```kotlin
if (techniques.contains(nouvelleTechnique)) {
    println("${espece.nom} connaît déjà ${nouvelleTechnique.nom} !")
    return
}

if (techniques.size < 3) {
    techniques.add(nouvelleTechnique)
    println("${espece.nom} a appris ${nouvelleTechnique.nom} !")
    return
}
```

**Early returns** : Sortie rapide si pas besoin de continuer

**Boucle de validation d'entrée utilisateur :**

```kotlin
var choix: Int
do {
    print("Votre choix : ")
    choix = readLine()?.toIntOrNull() ?: -1
    if (choix == 0) {
        println("Apprentissage annulé.")
        return
    }
    if (choix !in 1..techniques.size) {
        println("Choix invalide.")
    }
} while (choix !in 1..techniques.size)
```

**Points clés :**

- `readLine()?.toIntOrNull() ?: -1` : Gestion sûre de l'entrée (nullable + elvis operator)
- `choix !in 1..techniques.size` : Vérification d'appartenance à une range
- Boucle `do-while` : Redemande tant que l'entrée est invalide

**Suppression et ajout :**

```kotlin
val techniqueOubliee = techniques.removeAt(choix - 1)
println("${espece.nom} a oublié ${techniqueOubliee.nom} !")

techniques.add(nouvelleTechnique)
```

##### Méthode `afficheDetail()` - Affichage sophistiqué

**Préparation des données :**

```kotlin
val art = espece.afficheArt()
val artLines = art.lines()

val details = listOf(
    "Nom : $nom",
    "Niveau : $niveau",
    "PV : $pv / $pvMax",
    // ...
    "Potentiel : ${"%.2f".format(potentiel)}",
    "Expérience : ${"%.2f".format(exp)}"
)
```

**Points techniques :**

- `.lines()` : Découpe une string en liste de lignes
- `"%.2f".format(potentiel)` : Formatage à 2 décimales

**Algorithme d'affichage côte à côte :**

```kotlin
val maxArtWidth = artLines.maxOfOrNull { it.length } ?: 0
val maxLines = maxOf(artLines.size, details.size)

for (i in 0 until maxLines) {
    val artLine = if (i < artLines.size) artLines[i] else ""
    val detailLine = if (i < details.size) details[i] else ""
    println(artLine.padEnd(maxArtWidth + 4) + detailLine)
}
```

**Analyse détaillée :**

- `maxOfOrNull { it.length }` : Trouve la ligne la plus longue (avec gestion du cas vide)
- `maxOf(a, b)` : Prend le maximum entre deux valeurs
- `artLine.padEnd(maxArtWidth + 4)` : Ajoute des espaces à droite pour alignement
- Gestion des cas où les listes ont des tailles différentes avec `if (i < size)`

---

#### Classe `Element`

**Méthode `efficaciteContre()` - Expression when avancée :**

```kotlin
fun efficaciteContre(elementCible: Element): Double {
    return when {
        forces.contains(elementCible) -> 2.0
        faiblesses.contains(elementCible) -> 0.5
        immunises.contains(elementCible) -> 0.0
        else -> 1.0
    }
}
```

**Architecture :** Expression `when` retournant directement une valeur, très concis et lisible.

---

#### Classe `Technique`

##### Méthode `calculPrecision()`

**Génération et comparaison :**

```kotlin
fun calculPrecision(): Boolean {
    val nb = Random.nextInt(1, 101)  // Valeur entre 1 et 100
    return nb <= precision
}
```

**Simple mais efficace :** Compare directement sans condition if-else

##### Méthode `calculBonusStab()` - STAB System

**Calcul avec validation de minimum :**

```kotlin
fun calculBonusStab(attaquant: IndividuMonstre): Double {
    val aLesMemeElement = attaquant.espece.elements.contains(elementTechnique)
    return if (aLesMemeElement) {
        multiplicateurPuissance * 1.15
    } else {
        val resultat = multiplicateurPuissance * 0.85
        if (resultat < 0.1) 0.1 else resultat
    }
}
```

**Logique :**

- Bonus de 15% si même élément
- Malus de 15% sinon, mais avec plancher à 0.1

##### Méthode `effet()` - Calcul de dégâts complet

**Sélection de la stat appropriée :**

```kotlin
val degatsBase = if (estSpecial) attaquant.attaqueSpe else attaquant.attaque
```

**Gestion du double type :**

```kotlin
var multiElement = elementTechnique.efficaciteContre(defenseur.espece.elements[0])
if (defenseur.espece.elements.size > 1) {
    multiElement *= elementTechnique.efficaciteContre(defenseur.espece.elements[1])
}
```

**Point clé :** Multiplication des efficacités pour les doubles types (comme dans Pokémon)

---

### 3.3 Package `item`

#### Classe `MonsterKube`

**Héritage et interface simultanés :**

```kotlin
class MonsterKube(
    id: Int, nom: String, description: String,
    var chanceCapture: Double
) : Item(id, nom, description), Utilisable
```

**Méthode `utiliser()` - Formule de capture progressive :**

```kotlin
override fun utiliser(cible: IndividuMonstre): Boolean {
    if (cible.entraineur != null) {
        println("Le monstre ne peut pas être capturé.")
        return false
    }

    val ratioVie = cible.pv.toDouble() / cible.pvMax.toDouble()
    var chanceEffective = chanceCapture * (1.5 - ratioVie)
    chanceEffective = chanceEffective.coerceAtLeast(5.0)  // Minimum 5%

    val nbAleatoire = (0..100).random()

    return if (nbAleatoire < chanceEffective) {
        println("Le monstre est capturé !")
        true
    } else {
        println("Presque ! Le Kube n'a pas pu capturer le monstre !")
        false
    }
}
```

**Points techniques remarquables :**

- `.toDouble()` : Conversion pour division exacte (pas de division entière)
- `coerceAtLeast(5.0)` : Fonction Kotlin pour garantir un minimum
- `(0..100).random()` : Extension function sur Range

---

### 3.4 Package `monde`

#### Classe `Zone`

**Méthode `genereMonstre()` - Validation et randomisation :**

```kotlin
fun genereMonstre(): IndividuMonstre {
    if (especesMonstres.isEmpty()) {
        throw IllegalStateException("Aucune espèce disponible dans la zone $nom")
    }

    val especeChoisie = especesMonstres.random()
    val variation = Random.nextDouble(0.8, 1.2)  // +/- 20%
    val expInit = expZone * variation

    return IndividuMonstre(
        id = Random.nextInt(1000, 9999),
        nom = especeChoisie.nom,
        expInit = expInit,
        espece = especeChoisie,
        entraineur = null
    )
}
```

**Analyse :**

- `throw IllegalStateException()` : Exception custom avec message descriptif
- `.random()` : Extension function sur List
- `Random.nextDouble(0.8, 1.2)` : Variation ±20%

**Méthode `rencontreMonstre()` - Recherche conditionnelle :**

```kotlin
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
```

**Point clé :** `firstOrNull { it.pv > 0 }` - Trouve le premier monstre vivant, retourne null si aucun

---

### 3.5 Package `jeu`

#### Classe `CombatMonstre`

**Méthode `gameOver()` - Vérification avec `all` :**

```kotlin
fun gameOver(): Boolean {
    return joueur.equipeMonstre.all { it.pv <= 0 }
}
```

**Élégance :** Une seule ligne avec fonction d'ordre supérieur `all()`

**Méthode `joueurGagne()` - Multiples conditions de victoire :**

```kotlin
fun joueurGagne(): Boolean {
    if (monstreSauvage.pv <= 0) {
        println("${joueur.nom} a gagné !")
        val gainExp = monstreSauvage.exp * 0.20
        monstreJoueur.exp += gainExp
        println("${monstreJoueur.nom} gagne $gainExp exp")
        return true
    }

    if (monstreSauvage.entraineur == joueur) {
        println("${monstreSauvage.nom} a été capturé !")
        return true
    }

    return false
}
```

**Deux cas de victoire :**

1. Monstre KO → Gain d'expérience
2. Monstre capturé → Vérification par changement de propriétaire

**Méthode `actionJoueur()` - Menu avec polymorphisme :**

**Partie utilisation d'objet - Type checking :**

```kotlin
val itemChoisi = joueur.sacAItems[choixItem - 1]
if (itemChoisi is Utilisable) {
    itemChoisi.utiliser(monstreSauvage)
} else {
    println("Cet objet n'est pas utilisable en combat.")
}
```

**Analyse :** `is Utilisable` permet le smart cast automatique par Kotlin

**Partie changement de monstre - Validation en chaîne :**

```kotlin
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
```

**Double validation :**

1. L'entrée est un nombre valide
2. Le monstre choisi n'est pas KO

**Méthode `jouer()` - Système de vitesse :**

```kotlin
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
```

**Logique :** Le plus rapide attaque en premier, avec vérification de fin après chaque action

**Méthode `lanceCombat()` - Boucle principale :**

```kotlin
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
```

**Points clés :**

- Condition double dans le `while`
- `forEach { it.pv = it.pvMax }` : Restauration en une ligne

---

#### Classe `Partie`

**Méthode `choixStarter()` - Pattern matching avec when :**

```kotlin
val choix = readLine()?.toIntOrNull() ?: 1
val starter = when (choix) {
    1 -> monstre1
    2 -> monstre2
    else -> monstre3
}

starter.renommer()
joueur.equipeMonstre.add(starter)
starter.entraineur = joueur
```

**Utilisation de l'elvis operator :** `?: 1` fournit une valeur par défaut

**Méthode `modifierOrdreEquipe()` - Utilisation de Collections.swap :**

```kotlin
fun modifierOrdreEquipe() {
    if (joueur.equipeMonstre.size < 2) {
        println("Vous n'avez pas assez de monstres pour modifier l'ordre.")
        return
    }

    println("=== Ordre actuel ===")
    joueur.equipeMonstre.forEachIndexed { i, monstre ->
        println("${i+1} - ${monstre.nom}")
    }

    val pos1 = (readLine()?.toIntOrNull() ?: return) - 1
    val pos2 = (readLine()?.toIntOrNull() ?: return) - 1

    if (pos1 !in joueur.equipeMonstre.indices || pos2 !in joueur.equipeMonstre.indices) {
        println("Positions invalides.")
        return
    }

    Collections.swap(joueur.equipeMonstre, pos1, pos2)
}
```

**Points techniques :**

- `forEachIndexed` : Itération avec index et valeur
- `?: return` : Early return si l'entrée est invalide
- `!in collection.indices` : Vérification d'index valide
- `Collections.swap()` : Fonction Java pour échanger deux éléments

**Méthode `examineEquipe()` - Menu interactif :**

```kotlin
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
```

**Pattern `when (val x = ...)` :** Assigne et teste en même temps

---

## 4. Concepts de POO Implémentés

### 4.1 Encapsulation

**Exemple 1 : Setter avec validation et side-effect**

```kotlin
var exp: Double = 0.0
    set(value) {
        field = if (value < 0) 0.0 else value
        while (field >= palierExp(niveau + 1)) {
            levelUp()
        }
    }
```

**Avantages :**

- État toujours valide (pas d'exp négative)
- Side-effect automatique (level-up)
- Cohérence garantie

**Exemple 2 : Clamping automatique**

```kotlin
var pv: Int = 0
    set(nouveauPv) {
        field = when {
            nouveauPv < 0 -> 0
            nouveauPv > pvMax -> pvMax
            else -> nouveauPv
        }
    }
```

### 4.2 Héritage

**Déclaration de classe héritée :**

```kotlin
class MonsterKube(
    id: Int, nom: String, description: String,
    var chanceCapture: Double
) : Item(id, nom, description), Utilisable
```

**Points clés :**

- `: Item(...)` appelle le constructeur parent
- `, Utilisable` implémente l'interface
- Paramètres du parent passés directement

### 4.3 Polymorphisme

**Type checking et smart casting :**

```kotlin
if (itemChoisi is Utilisable) {
    itemChoisi.utiliser(monstreSauvage)  // Smart cast automatique
}
```

**Kotlin cast automatiquement après vérification `is`**

### 4.4 Composition

**Relations "has-a" :**

```kotlin
class Entraineur (
    var equipeMonstre: MutableList<IndividuMonstre> = mutableListOf(),
    var sacAItems: MutableList<Item> = mutableListOf()
)
```

**Avantage :** Flexibilité maximale, couplage faible

---

## 5. Systèmes de Jeu

### 5.1 Système de Progression

**Formule d'expérience quadratique :**

```kotlin
fun palierExp(niveau: Int): Double {
    return 100 * (niveau - 1).toDouble().pow(2.0)
}
```

**Tableau de progression :**

| Niveau | Exp Requise | Différence |
| ------ | ----------- | ---------- |
| 1      | 0           | -          |
| 2      | 100         | 100        |
| 3      | 400         | 300        |
| 4      | 900         | 500        |
| 5      | 1600        | 700        |
| 10     | 8100        | 2500       |

**Calcul des stats avec formule complexe :**

```kotlin
fun calcStat(baseStat: Int, modCarac: Double, potentiel: Double, randomRange: IntRange): Int {
    val statBonus = (modCarac * potentiel).roundToInt() +
                    Random.nextInt(randomRange.first, randomRange.last + 1)
    return baseStat + statBonus
}
```

**Exemple concret :**

```
Springleaf niveau 5, potentiel 1.5
attaque = 60 + (34.0 × 1.5).roundToInt() + random(-2, 2)
        = 60 + 51 + random
        = 109 à 113
```

### 5.2 Système de Combat

**Calcul de dégâts avec STAB et efficacité :**

```kotlin
val degatsBase = if (estSpecial) attaquant.attaqueSpe else attaquant.attaque
val multiplicateur = calculBonusStab(attaquant)  // 1.15 ou 0.85

var multiElement = elementTechnique.efficaciteContre(defenseur.espece.elements[0])
if (defenseur.espece.elements.size > 1) {
    multiElement *= elementTechnique.efficaciteContre(defenseur.espece.elements[1])
}

return (degatsBase * multiplicateur) * multiElement
```

**Exemple complet :**

```
Flamkip (Feu, Att.Spé = 13) → Flammèche (Feu, ×1.2) → Springleaf (Plante)

1. Base : 13
2. STAB : 1.2 × 1.15 = 1.38
3. Efficacité : 2.0 (Feu vs Plante)
4. Total : 13 × 1.38 × 2.0 = 35.88 dégâts
```

### 5.3 Système de Capture

**Formule progressive :**

```kotlin
val ratioVie = cible.pv.toDouble() / cible.pvMax.toDouble()
var chanceEffective = chanceCapture * (1.5 - ratioVie)
chanceEffective = chanceEffective.coerceAtLeast(5.0)
```

**Tableau d'exemples avec MonsterKube (30% de base) :**

| PV de la cible | Ratio | Calcul                    | Chance finale |
| -------------- | ----- | ------------------------- | ------------- |
| 100/100 (100%) | 1.0   | 30 × (1.5 - 1.0) = 15%    | 15%           |
| 50/100 (50%)   | 0.5   | 30 × (1.5 - 0.5) = 30%    | 30%           |
| 10/100 (10%)   | 0.1   | 30 × (1.5 - 0.1) = 42%    | 42%           |
| 1/100 (1%)     | 0.01  | 30 × (1.5 - 0.01) = 44.7% | 44.7%         |

**Analyse :** Plus le monstre est affaibli, plus la capture est facile, avec un minimum garanti de 5%

### 5.4 Système de Types Élémentaires

**Configuration dans le Main :**

```kotlin
// 🔥 Feu
feu.forces.addAll(listOf(plante, insecte))
feu.faiblesses.addAll(listOf(eau, roche, feu))

// 🌱 Plante
plante.forces.addAll(listOf(eau, roche))
plante.faiblesses.addAll(listOf(feu, insecte))
```

**Tableau des efficacités :**

|         | Feu  | Plante | Eau  | Insecte | Roche | Normal |
| ------- | ---- | ------ | ---- | ------- | ----- | ------ |
| Feu     | ×0.5 | ×2.0   | ×0.5 | ×2.0    | ×0.5  | ×1.0   |
| Plante  | ×0.5 | ×1.0   | ×2.0 | ×0.5    | ×2.0  | ×1.0   |
| Eau     | ×2.0 | ×0.5   | ×1.0 | ×1.0    | ×2.0  | ×1.0   |
| Insecte | ×0.5 | ×2.0   | ×1.0 | ×1.0    | ×0.5  | ×1.0   |
| Roche   | ×2.0 | ×0.5   | ×0.5 | ×2.0    | ×1.0  | ×1.0   |
| Normal  | ×1.0 | ×1.0   | ×1.0 | ×1.0    | ×0.5  | ×1.0   |

---

## 6. Exemples de Code Remarquables

### 6.1 Fonction Utilitaire de Coloration

**Utilisation de `when` pour mapping :**

```kotlin
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
```

**Points techniques :**

- `.lowercase()` : Méthode d'extension Kotlin sur String
- Codes ANSI : `\u001B[XXm` pour coloration terminal
- Expression ternaire : `if (...) x else y` retourne une valeur

**Utilisation élégante :**

```kotlin
println("Hello ${changeCouleur("my", "jaune")} World")
```

### 6.2 Pattern Builder avec Named Parameters

**Création déclarative d'espèces :**

```kotlin
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
```

**Avantages Kotlin :**

- Named parameters : Clarté même avec beaucoup de paramètres
- Ordre flexible
- Auto-documentation du code

### 6.3 Liaison de Zones - Pattern de Navigation

**Configuration bidirectionnelle :**

```kotlin
val route1 = Zone(id = 1, nom = "Route 1", expZone = 20, ...)
val route2 = Zone(id = 2, nom = "Route 2", expZone = 25, ...)

route1.zoneSuivante = route2
route2.zonePrecedente = route1
```

**Utilisation dans la navigation :**

```kotlin
"3" -> if (zone.zoneSuivante != null) {
    zone = zone.zoneSuivante!!
    println("Vous avancez vers ${zone.nom}")
}
"4" -> if (zone.zonePrecedente != null) {
    zone = zone.zonePrecedente!!
    println("Vous revenez vers ${zone.nom}")
}
```

**Point technique :** Opérateur `!!` (non-null assertion) après vérification

### 6.4 Initialisation du Jeu - Flow Control

**Fonction d'initialisation avec interaction :**

```kotlin
fun nouvellePartie(): Partie {
    println("Bienvenue dans le monde des monstres !")
    println("Quel est ton nom ?")
    val nomChoisi = readln()
    joueur.nom = nomChoisi

    println("Enchanté $nomChoisi ! Ton aventure commence maintenant...")

    return Partie(4, rival, route1)
}
```

**Séquence d'initialisation dans main :**

```kotlin
val partie = nouvellePartie()
partie.choixStarter()
partie.jouer()
```

**Architecture :** Enchaînement fluide des étapes d'initialisation

### 6.5 Configuration des Éléments - Lazy Initialization

**Initialisation après création :**

```kotlin
// Création des éléments
val feu = Element(1, "Feu")
val plante = Element(2, "Plante")
val eau = Element(3, "Eau")

// Configuration des relations (plus tard dans main)
feu.forces.addAll(listOf(plante, insecte))
feu.faiblesses.addAll(listOf(eau, roche, feu))

plante.forces.addAll(listOf(eau, roche))
plante.faiblesses.addAll(listOf(feu, insecte))
```

**Avantage :** Évite les références circulaires lors de la construction

### 6.6 Affichage Formaté - String Templates

**Interpolation complexe :**

```kotlin
println("${index + 1}. ${monstre.nom} (PV: ${monstre.pv}/${monstre.pvMax})")
```

**Formatage de nombres :**

```kotlin
"Potentiel : ${"%.2f".format(potentiel)}"
"Expérience : ${"%.2f".format(exp)}"
```

**Point clé :** `${ }` permet d'exécuter du code dans une string

---

## 7. Tests et Validation

### 7.1 Tests dans Main - Approche Systématique

**Test 1 : Affichage d'Art ASCII**

```kotlin
println("=== SPRINGLEAF FRONT ===")
println(especeSpringleaf.afficheArt(deFace = true))
println("=== SPRINGLEAF BACK ===")
println(especeSpringleaf.afficheArt(deFace = false))
```

**Validation :** ✓ Chargement de fichiers et codes ANSI fonctionnels

**Test 2 : Création et Niveau Initial**

```kotlin
val monstre1 = IndividuMonstre(1, "springleaf", 1500.0, especeSpringleaf)
println("=== Détails initiaux ===")
monstre1.afficheDetail()
```

**Ce qui est testé :**

- Constructeur avec expInit élevée
- Calcul automatique du niveau initial
- Variation aléatoire des stats

**Test 3 : System de Level-Up**

```kotlin
println("\n=== Test Level Up ===")
monstre1.exp += 1000.0
monstre1.afficheDetail()
```

**Points validés :**

- Déclenchement automatique du level-up via setter
- Recalcul des stats
- Affichage du nouveau niveau

**Test 4 : Clamping des PV**

```kotlin
println("\n=== Test PV Clamp ===")
monstre2.pv -= 999
println("PV de ${monstre2.nom} après dégâts massifs : ${monstre2.pv}")
monstre2.pv += 999
println("PV de ${monstre2.nom} après soin excessif : ${monstre2.pv}/${monstre2.pvMax}")
```

**Résultats attendus :**

```
Après -999 : pv = 0 (pas négatif)
Après +999 : pv = pvMax (pas supérieur au maximum)
```

**Test 5 : Système de Combat**

```kotlin
println("\n=== Test Attaque ===")
monstre1.attaquer(monstre3)
println("PV de ${monstre3.nom} après attaque : ${monstre3.pv}/${monstre3.pvMax}")
```

**Validation :**

- Calcul des dégâts : `attaque - (defense / 2)`
- Minimum de 1 dégât garanti
- Application correcte sur la cible

**Test 6 : Gestion des Collections**

```kotlin
println("\n=== Test Entraineur ===")
joueur.equipeMonstre.add(monstre1)
joueur.equipeMonstre.add(monstre2)
joueur.boiteMonstre.add(monstre3)
joueur.afficheDetail()
```

**Points vérifiés :**

- Ajout correct dans différentes listes
- Affichage avec `joinToString`

### 7.2 Test de Capture

```kotlin
val kubeBasique = MonsterKube(10, "MonsterKube", "Une sphère pour capturer les monstres", 30.0)
val monstre = IndividuMonstre(
    id = 4,
    nom = "Flamkip",
    expInit = 1500.0,
    espece = especeFlamkip,
    entraineur = null
)
kubeBasique.utiliser(monstre)
```

**Scénarios testés :**

- Tentative de capture sur monstre libre (entraineur = null)
- Calcul probabiliste de capture
- Messages de feedback appropriés

### 7.3 Test de Combat Complet

```kotlin
val monstreJoueur = IndividuMonstre(101, "FLAM", 0.0, flamkip)
val monstreSauvage = IndividuMonstre(201, "AQUA", 0.0, aquamy)

val joueur = Entraineur(1, "Sacha", 500)
joueur.equipeMonstre.add(monstreJoueur)

val combat = CombatMonstre(joueur, monstreJoueur, monstreSauvage)
combat.lanceCombat()
```

**Fonctionnalités testées :**

- Système de rounds
- Ordre d'action basé sur la vitesse
- Menu d'actions (attaquer, objet, changer)
- Conditions de victoire/défaite
- Gain d'expérience

### 7.4 Test d'Intégration - Partie Complète

```kotlin
val partie = nouvellePartie()
partie.choixStarter()
partie.jouer()
```

**Flow complet testé :**

1. Demande du nom du joueur
2. Choix du starter parmi 3 options
3. Renommage optionnel
4. Boucle de jeu principale avec navigation

---

**Fin du Rapport**

_Ce projet illustre une compréhension approfondie de la programmation orientée objet en Kotlin et démontre des compétences professionnelles en conception logicielle, architecture de code et développement de systèmes complexes avec une attention particulière portée à la qualité, la robustesse et la maintenabilité du code._
