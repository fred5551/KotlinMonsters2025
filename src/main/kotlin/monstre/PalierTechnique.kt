package org.example.monstre

class PalierTechnique( val id: Int,
                       val niveauRequis: Int,
                       val technique: Technique) {
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

}