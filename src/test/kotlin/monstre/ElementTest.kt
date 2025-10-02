package monstre

import org.example.eau
import org.example.especeFlamkip
import org.example.especeSpringleaf
import org.example.feu
import org.example.insecte
import org.example.monstre.EspeceMonstre
import org.example.monstre.IndividuMonstre
import org.example.monstre.Technique
import org.example.normal
import org.example.plante
import org.example.roche
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals



class ElementTest {
    @BeforeTest
    fun valorisation() {
        // 🔥 Feu
        feu.forces.addAll(listOf(plante, insecte))
        feu.faiblesses.addAll(listOf(eau, roche, feu))

        // 🌱 Plante
        plante.forces.addAll(listOf(eau, roche))
        plante.faiblesses.addAll(listOf(feu, insecte))

        // 💧 Eau
        eau.forces.addAll(listOf(feu, roche))
        eau.faiblesses.addAll(listOf(plante))

        // 🐞 Insecte
        insecte.forces.addAll(listOf(plante))
        insecte.faiblesses.addAll(listOf(feu, roche))

        // 🪨 Roche
        roche.forces.addAll(listOf(feu, insecte))
        roche.faiblesses.addAll(listOf(eau, plante))

        // ⚪ Normal
        normal.faiblesses.add(roche)
    }
    @Test
    fun efficaciteContre() {
        assertEquals(1.0, feu.efficaciteContre(normal))
        assertEquals(2.0, feu.efficaciteContre(plante))
        assertEquals(0.5, feu.efficaciteContre(feu))
        assertEquals(0.5, feu.efficaciteContre(eau))
        assertEquals(2.0, insecte.efficaciteContre(plante))
    }

    @Test
    fun nouveauxScenarios() {
        assertEquals(2.0, eau.efficaciteContre(feu))   // Eau > Feu
        assertEquals(0.5, plante.efficaciteContre(feu)) // Plante < Feu
        assertEquals(1.0, normal.efficaciteContre(plante)) // Normal neutre contre Plante
    }

    @Test
    fun testCalculPrecision() {
        val technique100 = Technique(1, "T100", 100.0, 1.0, false, false, false, true, normal)
        val technique0 = Technique(2, "T0", 0.0, 1.0, false, false, false, true, normal)
        val technique50 = Technique(3, "T50", 50.0, 1.0, false, false, false, true, normal)

        var compteurT50 = 0

        repeat(100) {
            // précision 100 → toujours vrai
            assertTrue(technique100.calculPrecision())

            // précision 0 → toujours faux
            assertFalse(technique0.calculPrecision())

            // précision 50 → environ une fois sur deux
            if (technique50.calculPrecision()) compteurT50++
        }

        println("compteurT50 = $compteurT50")
        assertTrue(compteurT50 > 20 && compteurT50 < 80)

        @Test
        fun testCalculBonusStab() {
            val techFeu = Technique(4, "Feu", 100.0, 50.0, true, false, false, true, feu)

            val monstreFeu = IndividuMonstre(1, "Flamkip", 1.0, especeFlamkip)
            val monstrePlante = IndividuMonstre(2, "SpringLeaf", 1.0, especeSpringleaf)

            // STAB appliqué
            val bonusFeu = techFeu.calculBonusStab(monstreFeu)
            assertEquals(57.5, bonusFeu) // 50 * 1.15 = 57.5

            // Malus appliqué
            val bonusPlante = techFeu.calculBonusStab(monstrePlante)
            assertEquals(42.5, bonusPlante) // 50 * 0.85 = 42.5
        }

    }
    @Test
    fun testEffetInfligeDegats() {
        val techPlante = Technique(5, "Technique plante", 100.0, 0.85, false, false, false, true, plante)

        val monstreFeu = IndividuMonstre(1, "attaquant", 1.0, especeFlamkip)
        val monstrePlante = IndividuMonstre(2, "defenseur", 1.0, especeSpringleaf)

        monstreFeu.attaque = 10
        monstrePlante.attaque = 10

        val degats1 = techPlante.effet(monstreFeu, monstrePlante)
        println(degats1)
        assertEquals(8.5, degats1)

        val degats2 = techPlante.effet(monstrePlante, monstreFeu)
        println(degats2)
        assertEquals(5.75, degats2)

        // TODO : Faire un test pour une attaque spéciale
    }

}