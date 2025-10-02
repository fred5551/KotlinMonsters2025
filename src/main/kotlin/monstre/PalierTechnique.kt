package org.example.monstre



class PalierTechnique(val id: Int,
                      val niveauRequis: Int,
                      val techniques: Technique) {
    /**
     * Vérifie si un individu monstre peut apprendre une technique
     * en fonction de son niveau.
     *
     * @param individu L'individu monstre à vérifier
     * @return true si le niveau de l'individu correspond exactement au niveau requis,
     *         false sinon
     */
    fun peutApprendre(individu: IndividuMonstre): Boolean {
        return individu.niveau == niveauRequis
    }
    /**
     * Permet à un monstre de maîtriser une nouvelle technique.
     */
    fun apprendreTechnique(nouvelleTechnique: Technique) {
        // Vérifier si déjà connue
        if (techniques.contains(nouvelleTechnique)) {
            println("${espece.nom} connaît déjà ${nouvelleTechnique.nom} !")
            return
        }

        // Cas : moins de 3 techniques
        if (techniques.size < 3) {
            techniques.add(nouvelleTechnique)
            println("${espece.nom} a appris ${nouvelleTechnique.nom} !")
            return
        }

        // Cas : il faut oublier une technique
        println("${espece.nom} veut apprendre ${nouvelleTechnique.nom}, mais connaît déjà 3 techniques.")
        println("Quelle technique souhaitez-vous oublier ? (0 pour annuler)")

        for ((index, tech) in techniques.withIndex()) {
            println("${index + 1}. ${tech.nom}")
        }

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

        val techniqueOubliee = techniques.removeAt(choix - 1)
        println("${espece.nom} a oublié ${techniqueOubliee.nom} !")

        techniques.add(nouvelleTechnique)
        println("${espece.nom} a appris ${nouvelleTechnique.nom} !")
    }

}