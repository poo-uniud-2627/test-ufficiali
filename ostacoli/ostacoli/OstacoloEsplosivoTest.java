// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 4: i test ufficiali dell'ostacolo esplosivo.
package ostacoli;

import static ostacoli.Celle.cella;
import static org.junit.jupiter.api.Assertions.*;

import geometria.Rettangolo;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class OstacoloEsplosivoTest {

    private static OstacoloFragile fragile(int riga, int colonna, int colpi) {
        return new OstacoloFragile(cella(riga, colonna), colpi);
    }

    @Test
    void daSoloSiRompeAlPrimoColpo() {
        Ostacolo e = new OstacoloEsplosivo(cella(0, 0));
        Ostacoli campo = new Ostacoli(List.of(e));
        assertFalse(e.isRotto());
        e.colpisci(campo);
        assertTrue(e.isRotto());
    }

    @Test
    void haIlSuoColorePrimaEDopoEStaFermo() {
        Ostacolo e = new OstacoloEsplosivo(cella(1, 1));
        Ostacoli campo = new Ostacoli(List.of(e));
        assertEquals(OstacoloEsplosivo.COLORE, e.getColore());
        e.avanza();
        e.colpisci(campo);
        e.avanza();
        assertEquals(OstacoloEsplosivo.COLORE, e.getColore());
        assertEquals(cella(1, 1), e.getArea());
    }

    @Test
    void colpitoDaRottoLanciaIllegalStateException() {
        Ostacolo e = new OstacoloEsplosivo(cella(0, 0));
        Ostacoli campo = new Ostacoli(List.of(e));
        e.colpisci(campo);
        // assertThrows è verde solo se il codice fra le parentesi lancia un'eccezione di quel tipo:
        // il contratto di colpisci promette IllegalStateException se l'ostacolo è già rotto.
        assertThrows(IllegalStateException.class, () -> e.colpisci(campo));
        assertTrue(e.isRotto());
    }

    @Test
    void rompeGliOttoOstacoliAttorno() {
        List<Ostacolo> tutti = new ArrayList<>();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (r == 1 && c == 1) tutti.add(new OstacoloEsplosivo(cella(1, 1)));
                else tutti.add(fragile(r, c, 1));
            }
        }
        Ostacoli campo = new Ostacoli(tutti);
        tutti.get(4).colpisci(campo);
        for (Ostacolo o : tutti) assertTrue(o.isRotto(), "non rotto: " + o.getArea());
    }

    @Test
    void arrivaFinoAPortataENonOltre() {
        Ostacolo e = new OstacoloEsplosivo(new Rettangolo(0, 0, 20, 40));
        Ostacolo giusto = new OstacoloFragile(new Rettangolo(50, 0, 20, 40), 1);     // a 30 pixel: tocca la zona
        Ostacolo oltre = new OstacoloFragile(new Rettangolo(0, 71, 20, 40), 1);      // a 31 pixel: no
        Ostacoli campo = new Ostacoli(List.of(oltre, e, giusto));
        e.colpisci(campo);
        assertTrue(giusto.isRotto());
        assertFalse(oltre.isRotto());
    }

    @Test
    void colpisceIViciniUnaVoltaSola() {
        Ostacolo e = new OstacoloEsplosivo(cella(0, 0));
        OstacoloFragile vicino = fragile(0, 1, 3);
        Ostacoli campo = new Ostacoli(List.of(e, vicino));
        e.colpisci(campo);
        assertEquals(2, vicino.getColpiRimasti());
    }

    @Test
    void esplodeACatena() {
        Ostacolo primo = new OstacoloEsplosivo(cella(0, 0));
        Ostacolo secondo = new OstacoloEsplosivo(cella(0, 1));
        Ostacolo ultimo = fragile(0, 2, 1);   // lo raggiunge solo l'esplosione del secondo
        Ostacoli campo = new Ostacoli(List.of(ultimo, secondo, primo));
        primo.colpisci(campo);
        assertTrue(secondo.isRotto());
        assertTrue(ultimo.isRotto());
    }

    @Test
    void dueEsplosiviAccantoNonSiColpisconoAllInfinito() {
        Ostacolo a = new OstacoloEsplosivo(cella(0, 0));
        Ostacolo b = new OstacoloEsplosivo(cella(0, 1));
        Ostacoli campo = new Ostacoli(List.of(a, b));
        b.colpisci(campo);
        assertTrue(a.isRotto());
        assertTrue(b.isRotto());
    }

    @Test
    void unFragileFraDueEsplosioniPrendeDueColpi() {
        Ostacolo primo = new OstacoloEsplosivo(cella(0, 0));
        Ostacolo secondo = new OstacoloEsplosivo(cella(1, 0));
        OstacoloFragile fragile = fragile(0, 1, 3);   // tocca le zone di tutti e due
        Ostacoli campo = new Ostacoli(List.of(primo, fragile, secondo));
        primo.colpisci(campo);
        assertTrue(secondo.isRotto());
        assertEquals(1, fragile.getColpiRimasti());
    }

    @Test
    void iFissiEIMobiliVicinoNonSiRompono() {
        Ostacolo e = new OstacoloEsplosivo(cella(0, 0));
        Ostacolo fisso = new OstacoloFisso(cella(0, 1));
        Ostacolo mobile = new OstacoloMobile(cella(1, 0), 2, 0, 200);
        Ostacoli campo = new Ostacoli(List.of(fisso, e, mobile));
        assertDoesNotThrow(() -> e.colpisci(campo));
        assertFalse(fisso.isRotto());
        assertFalse(mobile.isRotto());
        assertEquals(cella(1, 0), mobile.getArea());
    }
}
