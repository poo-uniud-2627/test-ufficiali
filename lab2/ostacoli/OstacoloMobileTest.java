// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 4: i test ufficiali dell'ostacolo mobile.
package ostacoli;

import static org.junit.jupiter.api.Assertions.*;

import geometria.Rettangolo;
import java.util.List;
import org.junit.jupiter.api.Test;

class OstacoloMobileTest {

    /** Un mobile alto 20, con il bordo alto a y = 100, che va a 4 pixel per passo fra y = 50 e y = 150. */
    private static OstacoloMobile mobile() {
        return new OstacoloMobile(new Rettangolo(10, 100, 16, 20), 4, 50, 150);
    }

    private static void avanza(Ostacolo o, int volte) {
        for (int i = 0; i < volte; i++) o.avanza();
    }

    @Test
    void nuovoStaDovEENonERotto() {
        Ostacolo m = mobile();
        assertEquals(new Rettangolo(10, 100, 16, 20), m.getArea());
        assertFalse(m.isRotto());
        assertEquals(OstacoloMobile.COLORE, m.getColore());
    }

    @Test
    void aOgniPassoScendeDiVelocitaPixel() {
        Ostacolo m = mobile();
        m.avanza();
        assertEquals(new Rettangolo(10, 104, 16, 20), m.getArea());
        m.avanza();
        assertEquals(new Rettangolo(10, 108, 16, 20), m.getArea());
    }

    @Test
    void lAreaCambiaMaRestaDellaStessaGrandezzaENonSiSpostaDiLato() {
        Ostacolo m = mobile();
        Rettangolo prima = m.getArea();
        avanza(m, 7);
        Rettangolo dopo = m.getArea();
        assertNotEquals(prima, dopo);
        assertEquals(prima.x(), dopo.x());
        assertEquals(prima.larghezza(), dopo.larghezza());
        assertEquals(prima.altezza(), dopo.altezza());
    }

    @Test
    void arrivaAlLimiteBassoSiFermaLiETornaSu() {
        Ostacolo m = mobile();   // bordo basso a 120: 30 pixel dal limite, che non è un multiplo di 4
        avanza(m, 7);
        assertEquals(148, m.getArea().y() + m.getArea().altezza());
        m.avanza();              // con 4 pixel andrebbe a 152: si ferma sul limite
        assertEquals(150, m.getArea().y() + m.getArea().altezza());
        m.avanza();              // e torna su
        assertEquals(146, m.getArea().y() + m.getArea().altezza());
    }

    @Test
    void arrivaAlLimiteAltoSiFermaLiETornaGiu() {
        Ostacolo m = mobile();
        avanza(m, 9);            // in fondo e di nuovo su: bordo alto a 126
        assertEquals(126, m.getArea().y());
        avanza(m, 18);           // sale fino a 54
        assertEquals(54, m.getArea().y());
        m.avanza();              // con 4 pixel andrebbe a 50: arriva proprio sul limite
        assertEquals(50, m.getArea().y());
        m.avanza();
        assertEquals(54, m.getArea().y());
    }

    @Test
    void arrivatoEsattamenteSulLimiteAlPassoDopoTornaIndietro() {
        // bordo basso a 120, limite a 140: 20 pixel, cinque passi da 4 esatti
        Ostacolo m = new OstacoloMobile(new Rettangolo(0, 100, 16, 20), 4, 50, 140);
        avanza(m, 5);
        assertEquals(140, m.getArea().y() + m.getArea().altezza());
        m.avanza();
        assertEquals(136, m.getArea().y() + m.getArea().altezza(), "sul limite non resta fermo un passo: torna su");
    }

    @Test
    void nonEsceMaiDaiLimiti() {
        Ostacolo m = new OstacoloMobile(new Rettangolo(0, 70, 16, 30), 7, 60, 200);
        double piuAlto = Double.MAX_VALUE;
        double piuBasso = -Double.MAX_VALUE;
        for (int i = 0; i < 1000; i++) {
            m.avanza();
            Rettangolo a = m.getArea();
            assertTrue(a.y() >= 60, "bordo alto sopra il limite al passo " + i + ": " + a);
            assertTrue(a.y() + a.altezza() <= 200, "bordo basso sotto il limite al passo " + i + ": " + a);
            piuAlto = Math.min(piuAlto, a.y());
            piuBasso = Math.max(piuBasso, a.y() + a.altezza());
        }
        assertEquals(60, piuAlto, "non arriva mai al limite alto");
        assertEquals(200, piuBasso, "non arriva mai al limite basso");
    }

    @Test
    void colpitoNonSiRompeMaiENonLanciaEccezioni() {
        Ostacolo m = mobile();
        Ostacoli campo = new Ostacoli(List.of(m));
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> m.colpisci(campo));
            assertFalse(m.isRotto());
            assertNotNull(m.getColore());
        }
    }

    @Test
    void ilColpoNonLoSpostaENonCambiaIlSuoMovimento() {
        Ostacolo colpito = mobile();
        Ostacolo mai = mobile();
        Ostacoli campo = new Ostacoli(List.of(colpito, mai));
        for (int i = 0; i < 40; i++) {
            colpito.colpisci(campo);
            assertEquals(mai.getArea(), colpito.getArea(), "al passo " + i);
            colpito.avanza();
            mai.avanza();
        }
    }

    @Test
    void ilContenitoreLoFaMuovereConAvanzaTutti() {
        Ostacolo m = mobile();
        Ostacoli campo = new Ostacoli(List.of(new OstacoloFisso(Celle.cella(3, 3)), m));
        campo.avanzaTutti();
        campo.avanzaTutti();
        assertEquals(new Rettangolo(10, 108, 16, 20), m.getArea());
    }
}
