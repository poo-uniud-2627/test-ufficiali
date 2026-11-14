// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Lezione 12: il test del contratto. Le promesse dell'interfaccia Ostacolo, provate su un ostacolo qualunque.
package ostacoli;

import static ostacoli.Celle.cella;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import geometria.Rettangolo;
import org.junit.jupiter.api.Test;

/**
 * I test di quello che promette ogni Ostacolo (src/ostacoli/Ostacolo.java). È astratta: JUnit non la
 * esegue da sola. Esegue questi test in ogni sua sottoclasse, e ogni sottoclasse dice solo quale
 * ostacolo provare, con il metodo nuovoOstacolo.
 */
abstract class ContrattoOstacoloTest {

    /** Un ostacolo nuovo, mai colpito, del tipo da provare, nell'area data. */
    abstract Ostacolo nuovoOstacolo(Rettangolo area);

    /** Quante volte si prova a colpire un ostacolo, al massimo. */
    static final int COLPI = 100;

    /** Un secondo di gioco: 60 chiamate di avanza, come fa la Partita in un secondo. */
    static void unSecondo(Ostacolo o) {
        for (int passo = 0; passo < 60; passo++) o.avanza();
    }

    /** Colpisce l'ostacolo finché non si rompe, al massimo COLPI volte, con un secondo di gioco fra un colpo e l'altro. */
    static void colpisciFinchéSiRompe(Ostacolo o, Ostacoli campo) {
        for (int i = 0; i < COLPI && !o.isRotto(); i++) {
            o.colpisci(campo);
            if (!o.isRotto()) unSecondo(o);
        }
    }

    @Test
    void areaEColoreNonSonoMaiNull() {
        Ostacolo o = nuovoOstacolo(cella(1, 1));
        Ostacoli campo = new Ostacoli(List.of(o));
        for (int i = 0; i < COLPI && !o.isRotto(); i++) {
            assertNotNull(o.getArea());
            assertNotNull(o.getColore());
            o.colpisci(campo);
            unSecondo(o);
        }
        assertNotNull(o.getArea());
        assertNotNull(o.getColore());
    }

    @Test
    void colpireUnOstacoloNonRottoNonLanciaEccezioni() {
        Ostacolo o = nuovoOstacolo(cella(1, 1));
        Ostacoli campo = new Ostacoli(List.of(o));
        for (int i = 0; i < COLPI && !o.isRotto(); i++) {
            assertDoesNotThrow(() -> o.colpisci(campo));   // un colpo dopo l'altro, nello stesso passo del gioco
        }
    }

    @Test
    void colpireUnRottoLanciaIllegalStateException() {
        Ostacolo o = nuovoOstacolo(cella(1, 1));
        Ostacoli campo = new Ostacoli(List.of(o));
        colpisciFinchéSiRompe(o, campo);
        if (!o.isRotto()) return;          // non si rompe mai: questa promessa non dice niente
        assertThrows(IllegalStateException.class, () -> o.colpisci(campo));
        assertTrue(o.isRotto());
    }

    @Test
    void unaVoltaRottoRestaRotto() {
        Ostacolo o = nuovoOstacolo(cella(1, 1));
        Ostacoli campo = new Ostacoli(List.of(o));
        colpisciFinchéSiRompe(o, campo);
        if (!o.isRotto()) return;          // non si rompe mai: questa promessa non dice niente
        for (int passo = 0; passo < 600; passo++) {   // 600 passi del gioco: dieci secondi
            o.avanza();
            assertTrue(o.isRotto(), "è tornato intero dopo " + (passo + 1) + " passi");
        }
    }
}
