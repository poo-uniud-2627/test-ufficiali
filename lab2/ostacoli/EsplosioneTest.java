// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 4: i test ufficiali di Ostacoli.colpisciAttorno.
package ostacoli;

import static ostacoli.Celle.cella;
import static org.junit.jupiter.api.Assertions.*;

import geometria.Rettangolo;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class EsplosioneTest {

    private static OstacoloFragile fragile(int riga, int colonna, int colpi) {
        return new OstacoloFragile(cella(riga, colonna), colpi);
    }

    @Test
    void colpisceSoloGliOstacoliCheToccanoLaZona() {
        Ostacolo a = fragile(0, 0, 1);
        Ostacolo b = fragile(0, 1, 1);
        Ostacolo c = fragile(0, 2, 1);
        Ostacoli campo = new Ostacoli(List.of(a, b, c));
        campo.colpisciAttorno(new Rettangolo(0, 0, 60, 10));   // tocca a e b, non c
        assertTrue(a.isRotto());
        assertTrue(b.isRotto());
        assertFalse(c.isRotto());
    }

    @Test
    void unaZonaCheNonToccaNienteNonCambiaNiente() {
        OstacoloFragile a = fragile(0, 0, 2);
        OstacoloFragile b = fragile(0, 1, 2);
        Ostacoli campo = new Ostacoli(List.of(a, b));
        campo.colpisciAttorno(new Rettangolo(0, 500, 50, 50));
        assertEquals(2, a.getColpiRimasti());
        assertEquals(2, b.getColpiRimasti());
    }

    @Test
    void ilBordoContaComeTocco() {
        Ostacolo a = fragile(0, 0, 1);   // va da x = 0 a x = 20
        Ostacoli campo = new Ostacoli(List.of(a));
        campo.colpisciAttorno(new Rettangolo(20, 0, 10, 10));
        assertTrue(a.isRotto());
    }

    @Test
    void nonColpisceGliOstacoliGiaRotti() {
        Ostacolo a = fragile(0, 0, 1);
        Ostacolo b = fragile(0, 1, 1);
        Ostacoli campo = new Ostacoli(List.of(a, b));
        a.colpisci(campo);
        // assertDoesNotThrow è verde se il codice fra le parentesi non lancia eccezioni:
        // colpire a, già rotto, lancerebbe IllegalStateException.
        assertDoesNotThrow(() -> campo.colpisciAttorno(new Rettangolo(0, 0, 100, 50)));
        assertTrue(b.isRotto());
    }

    @Test
    void colpisceOgniOstacoloUnaVoltaSola() {
        OstacoloFragile f = fragile(0, 0, 3);
        Ostacoli campo = new Ostacoli(List.of(f));
        campo.colpisciAttorno(new Rettangolo(0, 0, 200, 200));
        assertEquals(2, f.getColpiRimasti());
    }

    @Test
    void unOstacoloRottoDuranteLEsplosioneNonVieneColpitoDiNuovo() {
        Ostacolo esplosivo = new OstacoloEsplosivo(cella(0, 0));
        Ostacolo vicino = fragile(0, 1, 1);   // lo rompe l'esplosivo, prima che arrivi il suo turno
        Ostacoli campo = new Ostacoli(List.of(esplosivo, vicino));
        assertDoesNotThrow(() -> campo.colpisciAttorno(new Rettangolo(0, 0, 100, 50)));
        assertTrue(esplosivo.isRotto());
        assertTrue(vicino.isRotto());
    }

    @Test
    void unCampoDiEsplosiviSiRompeTuttoConUnColpo() {
        Ostacolo a = new OstacoloEsplosivo(cella(0, 0));
        Ostacolo b = new OstacoloEsplosivo(cella(0, 1));
        Ostacolo c = new OstacoloEsplosivo(cella(1, 0));
        Ostacolo d = new OstacoloEsplosivo(cella(1, 1));
        Ostacoli campo = new Ostacoli(List.of(a, b, c, d));
        d.colpisci(campo);
        for (Ostacolo o : campo.getOstacoli()) assertTrue(o.isRotto(), "non rotto: " + o.getArea());
    }

    @Test
    void nonChiamaNemmenoColpisciSugliOstacoliGiaRotti() {
        List<String> registro = new ArrayList<>();
        OstacoloSpia rotta = new OstacoloSpia(cella(0, 0), "rotta", registro);
        OstacoloSpia intera = new OstacoloSpia(cella(0, 1), "intera", registro);
        rotta.rompiPrima();
        Ostacoli campo = new Ostacoli(List.of(rotta, intera));
        campo.colpisciAttorno(new Rettangolo(0, 0, 100, 50));
        assertEquals(0, rotta.getChiamate(), "colpisci chiamato su un ostacolo già rotto");
        assertEquals(1, intera.getChiamate());
    }

    @Test
    void unaSpiaRottaACatenaVieneColpitaUnaVoltaSola() {
        List<String> registro = new ArrayList<>();
        Ostacolo esplosivo = new OstacoloEsplosivo(cella(0, 0));
        OstacoloSpia spia = new OstacoloSpia(cella(0, 1), "spia", registro);   // la rompe l'esplosione
        Ostacoli campo = new Ostacoli(List.of(esplosivo, spia));
        campo.colpisciAttorno(new Rettangolo(0, 0, 100, 50));
        assertEquals(1, spia.getChiamate(), "colpisci chiamato di nuovo sulla spia rotta a catena");
    }

    @Test
    void colpisceNellOrdineDellaLista() {
        List<String> registro = new ArrayList<>();
        Ostacolo terzo = new OstacoloSpia(cella(0, 0), "terzo", registro);
        Ostacolo primo = new OstacoloSpia(cella(0, 1), "primo", registro);
        Ostacolo secondo = new OstacoloSpia(cella(0, 2), "secondo", registro);
        Ostacoli campo = new Ostacoli(List.of(primo, secondo, terzo));
        campo.colpisciAttorno(new Rettangolo(0, 0, 200, 50));
        assertEquals(List.of("primo", "secondo", "terzo"), registro);
    }
}
