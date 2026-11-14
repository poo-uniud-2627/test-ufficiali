// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 5, variante 2 (le stelle): i test ufficiali di Stella, Raccolta e dei livelli.
// Ogni test è un caso del contratto.
package stelle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import geometria.Rettangolo;
import ostacoli.MotoreDeiLivelli;
import org.junit.jupiter.api.Test;

class StelleTest {

    private static final Rettangolo AREA = new Rettangolo(30, 100, 16, 16);

    private static void avanza(Stella s, int volte) {
        for (int i = 0; i < volte; i++) s.avanza();
    }

    // ---- Stella

    @Test
    void unaStellaNuovaSiVedeENonERaccolta() {
        Stella s = new Stella(AREA, 60);
        assertTrue(s.isVisibile());
        assertFalse(s.isRaccolta());
        assertEquals(AREA, s.getArea());
    }

    @Test
    void unaStellaSparisceDopoLaSuaDurata() {
        Stella s = new Stella(AREA, 60);
        avanza(s, 59);
        assertTrue(s.isVisibile());
        s.avanza();
        assertFalse(s.isVisibile());
        assertFalse(s.isRaccolta());
        avanza(s, 10);
        assertFalse(s.isVisibile());
    }

    @Test
    void unaStellaRaccoltaNonSiVedePiu() {
        Stella s = new Stella(AREA, 60);
        avanza(s, 30);
        s.raccogli();
        assertTrue(s.isRaccolta());
        assertFalse(s.isVisibile());
        s.avanza();
        assertFalse(s.isVisibile());
        assertTrue(s.isRaccolta());
    }

    @Test
    void raccogliereUnaStellaGiaRaccoltaLanciaIllegalStateException() {
        Stella s = new Stella(AREA, 60);
        s.raccogli();
        assertThrows(IllegalStateException.class, s::raccogli);
        assertTrue(s.isRaccolta());
    }

    @Test
    void raccogliereUnaStellaSparitaLanciaIllegalStateExceptionENonCambiaNiente() {
        Stella s = new Stella(AREA, 5);
        avanza(s, 5);
        assertThrows(IllegalStateException.class, s::raccogli);
        assertFalse(s.isRaccolta());
    }

    @Test
    void lAreaNonCambiaMai() {
        Stella s = new Stella(AREA, 3);
        avanza(s, 2);
        s.raccogli();
        avanza(s, 5);
        assertEquals(AREA, s.getArea());
    }

    @Test
    void ilCostruttoreDiStellaRifiutaGliArgomentiSbagliati() {
        assertThrows(IllegalArgumentException.class, () -> new Stella(null, 60));
        assertThrows(IllegalArgumentException.class, () -> new Stella(AREA, 0));
        assertThrows(IllegalArgumentException.class, () -> new Stella(AREA, -5));
    }

    @Test
    void unaStellaDiUnFotogrammaEAccettata() {
        Stella s = new Stella(AREA, 1);
        assertTrue(s.isVisibile());
        s.avanza();
        assertFalse(s.isVisibile());
    }

    // ---- Raccolta

    @Test
    void conLaQuintaStellaSiGuadagnaUnaVitaEIlContoTornaAZero() {
        Raccolta r = new Raccolta(5);
        for (int i = 1; i <= 4; i++) {
            assertFalse(r.aggiungi(), "stella " + i);
            assertEquals(i, r.getStelle());
        }
        assertTrue(r.aggiungi());
        assertEquals(0, r.getStelle());
    }

    @Test
    void ogniCinqueStelleUnaVita() {
        Raccolta r = new Raccolta(5);
        int vite = 0;
        for (int i = 0; i < 12; i++) if (r.aggiungi()) vite++;
        assertEquals(2, vite);
        assertEquals(2, r.getStelle());
    }

    @Test
    void conUnaStellaPerVitaOgniStellaEUnaVita() {
        Raccolta r = new Raccolta(1);
        assertTrue(r.aggiungi());
        assertTrue(r.aggiungi());
        assertEquals(0, r.getStelle());
    }

    @Test
    void ilCostruttoreDiRaccoltaRifiutaMenoDiUnaStella() {
        assertThrows(IllegalArgumentException.class, () -> new Raccolta(0));
        assertThrows(IllegalArgumentException.class, () -> new Raccolta(-1));
    }

    @Test
    void nelGiocoServonoCinqueStelle() {
        assertEquals(5, Raccolta.STELLE_PER_UNA_VITA);
    }

    // ---- i livelli

    @Test
    void piuSiSaleMenoDuraUnaStella() {
        int[] attese = {300, 270, 240, 210, 180};      // da 5 secondi a 3 secondi
        for (int livello = 1; livello <= 5; livello++) {
            assertEquals(attese[livello - 1], MotoreDeiLivelli.livello(livello, 800, 500).durataStella(), "livello " + livello);
        }
    }
}
