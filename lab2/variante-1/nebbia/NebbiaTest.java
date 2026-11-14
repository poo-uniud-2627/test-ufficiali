// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 5, variante 1 (la nebbia): i test ufficiali. Ogni test è un caso del contratto.
package nebbia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import geometria.Posizione;
import geometria.Rettangolo;
import ostacoli.MotoreDeiLivelli;
import org.junit.jupiter.api.Test;

class NebbiaTest {

    @Test
    void laStrisciaStaAlCentroEdeAltaComeIlCampo() {
        Rettangolo z = Nebbia.alCentro(800, 500, 0.30).getZona();
        assertEquals(240, z.larghezza(), 1e-9);
        assertEquals(500, z.altezza(), 1e-9);
        assertEquals(280, z.x(), 1e-9);
        assertEquals(0, z.y(), 1e-9);
    }

    @Test
    void nascondeIlCentroDelCampoENonLeRacchette() {
        Nebbia n = Nebbia.alCentro(800, 500, 0.40);
        assertTrue(n.nasconde(new Posizione(400, 250)));
        assertTrue(n.nasconde(new Posizione(400, 3)));
        assertFalse(n.nasconde(new Posizione(40, 250)));
        assertFalse(n.nasconde(new Posizione(760, 250)));
    }

    @Test
    void iBordiDellaStrisciaSonoNellaNebbia() {
        Nebbia n = Nebbia.alCentro(800, 500, 0.30);      // da x = 280 a x = 520
        assertTrue(n.nasconde(new Posizione(280, 100)));
        assertTrue(n.nasconde(new Posizione(520, 100)));
        assertFalse(n.nasconde(new Posizione(279.5, 100)));
        assertFalse(n.nasconde(new Posizione(520.5, 100)));
    }

    @Test
    void nessunaNebbiaNonNascondeNiente() {
        Nebbia n = Nebbia.nessuna();
        assertFalse(n.isPresente());
        assertFalse(n.nasconde(new Posizione(400, 250)));
        assertThrows(IllegalStateException.class, n::getZona);
    }

    @Test
    void conFrazioneZeroNonCeNebbia() {
        Nebbia n = Nebbia.alCentro(800, 500, 0);
        assertFalse(n.isPresente());
        assertFalse(n.nasconde(new Posizione(400, 250)));
    }

    @Test
    void argomentiSbagliatiLancianoIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Nebbia.alCentro(800, 500, -0.1));
        assertThrows(IllegalArgumentException.class, () -> Nebbia.alCentro(800, 500, 1));
        assertThrows(IllegalArgumentException.class, () -> Nebbia.alCentro(0, 500, 0.3));
        assertThrows(IllegalArgumentException.class, () -> Nebbia.alCentro(800, -1, 0.3));
        assertThrows(IllegalArgumentException.class, () -> Nebbia.alCentro(800, 500, 0.3).nasconde(null));
    }

    @Test
    void neiLivelliUnoEDueNonCeNebbia() {
        assertFalse(MotoreDeiLivelli.livello(1, 800, 500).nebbia().isPresente());
        assertFalse(MotoreDeiLivelli.livello(2, 800, 500).nebbia().isPresente());
    }

    @Test
    void dalLivelloTreLaNebbiaCeEAllargaAOgniLivello() {
        double[] attese = {240, 280, 320};               // 30%, 35%, 40% di 800
        for (int livello = 3; livello <= 5; livello++) {
            Nebbia n = MotoreDeiLivelli.livello(livello, 800, 500).nebbia();
            assertTrue(n.isPresente(), "livello " + livello);
            assertEquals(attese[livello - 3], n.getZona().larghezza(), 1e-9, "livello " + livello);
            assertTrue(n.nasconde(new Posizione(400, 250)), "livello " + livello);
        }
    }
}
