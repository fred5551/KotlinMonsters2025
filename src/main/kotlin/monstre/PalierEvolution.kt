package org.example.monstre

class PalierEvolution(
    var id: Int,
    var niveauRequis: Int,
    var evolution: EspeceMonstre

) {

    fun peutEvoluer(individuMonstre : IndividuMonstre): Boolean{
        if (individuMonstre.niveau >= 15 ) {
            return true
        }
        else {
            return false
        }
    }

}