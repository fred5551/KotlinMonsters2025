package org.example

import org.example.dresseur.Entraineur

var joueur = Entraineur(1,"Sacha",100)
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
}

