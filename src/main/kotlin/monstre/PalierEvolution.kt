package org.example.monstre

class PalierEvolution(
    var id: Int,
    var niveauRequis: Int,
    var evolution: EspeceMonstre

) {

    fun peutEvoluer(individu: IndividuMonstre): Boolean {
        return individu.niveau >= niveauRequis
    }

}