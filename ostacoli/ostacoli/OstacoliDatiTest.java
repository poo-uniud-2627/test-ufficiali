// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 4: i test dei due ostacoli già scritti e del contenitore. Se li riscrivete (per esempio
// dopo la lezione 10), devono continuare a mantenere il loro contratto.
package ostacoli;

import static ostacoli.Celle.cella;
import static org.junit.jupiter.api.Assertions.*;

import geometria.Rettangolo;
import java.awt.Color;
import java.util.List;
import org.junit.jupiter.api.Test;

class OstacoliDatiTest {

    @Test
    void ilFissoNonSiRompeMaiENonCambia() {
        Ostacolo f = new OstacoloFisso(cella(0, 0));
        Ostacoli campo = new Ostacoli(List.of(f));
        for (int i = 0; i < 10; i++) {
            f.colpisci(campo);
            f.avanza();
        }
        assertFalse(f.isRotto());
        assertEquals(cella(0, 0), f.getArea());
        assertEquals(OstacoloFisso.GRIGIO, f.getColore());
    }

    @Test
    void ilFragileSiRompeDopoIColpiDati() {
        OstacoloFragile f = new OstacoloFragile(cella(0, 0), 3);
        Ostacoli campo = new Ostacoli(List.of(f));
        assertEquals(3, f.getColpiRimasti());
        f.colpisci(campo);
        f.colpisci(campo);
        assertFalse(f.isRotto());
        assertEquals(1, f.getColpiRimasti());
        f.colpisci(campo);
        assertTrue(f.isRotto());
        assertEquals(0, f.getColpiRimasti());
    }

    @Test
    void ilFragileRottoLanciaIllegalStateExceptionENonCambia() {
        OstacoloFragile f = new OstacoloFragile(cella(0, 0), 1);
        Ostacoli campo = new Ostacoli(List.of(f));
        f.colpisci(campo);
        // il contratto di colpisci: se l'ostacolo è già rotto, IllegalStateException e niente cambia
        assertThrows(IllegalStateException.class, () -> f.colpisci(campo));
        assertTrue(f.isRotto());
        assertEquals(0, f.getColpiRimasti());
    }

    @Test
    void ilFragileSiSchiarisceAOgniColpo() {
        OstacoloFragile f = new OstacoloFragile(cella(0, 0), 3);
        Ostacoli campo = new Ostacoli(List.of(f));
        int prima = luce(f.getColore());
        for (int i = 0; i < 3; i++) {
            f.colpisci(campo);
            assertNotNull(f.getColore());
            assertTrue(luce(f.getColore()) > prima, "non più chiaro dopo il colpo " + (i + 1));
            prima = luce(f.getColore());
        }
    }

    @Test
    void ilContenitoreDaIlPrimoNonRottoCheTocca() {
        OstacoloFragile a = new OstacoloFragile(cella(0, 0), 1);
        OstacoloFragile b = new OstacoloFragile(cella(0, 1), 1);
        Ostacoli campo = new Ostacoli(List.of(a, b));
        Rettangolo traIDue = new Rettangolo(15, 10, 40, 10);   // tocca a e b
        assertSame(a, campo.ostacoloCheTocca(traIDue));
        a.colpisci(campo);
        assertSame(b, campo.ostacoloCheTocca(traIDue));
        b.colpisci(campo);
        assertNull(campo.ostacoloCheTocca(traIDue));
    }

    /** Quanto è chiaro un colore: la somma di rosso, verde e blu. */
    private static int luce(Color c) {
        return c.getRed() + c.getGreen() + c.getBlue();
    }
}
