package monstre

import org.example.eau
import org.example.feu
import org.example.insecte
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


}