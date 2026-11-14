// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 5, variante 3 (Pong su quattro lati): i test ufficiali. Ogni test è un caso del contratto.
package pong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ostacoli.Livello;
import ostacoli.MotoreDeiLivelli;
import org.junit.jupiter.api.Test;

class QuattroLatiTest {

    private static final Campo CAMPO = new Campo(800, 500);

    /** Un giocatore che sta fermo: nei test le racchette non si muovono da sole. */
    private static final Giocatore FERMO = (palla, racchetta) -> Mossa.FERMO;

    private static Partita partitaAlLivello(int numero) {
        Partita p = new Partita(CAMPO, FERMO);
        p.preparaLivello(numero);
        p.avvia();
        return p;
    }

    private static void aggiorna(Partita p, int volte) {
        for (int i = 0; i < volte; i++) p.aggiorna();
    }

    // ---- Bordo

    @Test
    void altoEBassoSonoOrizzontali() {
        assertTrue(Bordo.ALTO.orizzontale());
        assertTrue(Bordo.BASSO.orizzontale());
        assertFalse(Bordo.SINISTRA.orizzontale());
        assertFalse(Bordo.DESTRA.orizzontale());
    }

    @Test
    void ilBordoOppostoEQuelloDiFronte() {
        assertEquals(Bordo.DESTRA, Bordo.SINISTRA.opposto());
        assertEquals(Bordo.SINISTRA, Bordo.DESTRA.opposto());
        assertEquals(Bordo.BASSO, Bordo.ALTO.opposto());
        assertEquals(Bordo.ALTO, Bordo.BASSO.opposto());
    }

    // ---- Racchetta

    @Test
    void unaRacchettaVerticaleStaComePrima() {
        Racchetta r = new Racchetta(Bordo.SINISTRA, CAMPO);
        assertEquals(30, r.getX());
        assertEquals(210, r.getY(), 1e-9);
        assertEquals(12, r.getLarghezza());
        assertEquals(80, r.getAltezza());
        Racchetta d = new Racchetta(Bordo.DESTRA, CAMPO);
        assertEquals(800 - 30 - 12, d.getX());
    }

    @Test
    void unaRacchettaOrizzontaleStaAlCentroDelSuoBordo() {
        Racchetta alto = new Racchetta(Bordo.ALTO, CAMPO);
        assertEquals(80, alto.getLarghezza());
        assertEquals(12, alto.getAltezza());
        assertEquals(360, alto.getX());
        assertEquals(30, alto.getY(), 1e-9);
        Racchetta basso = new Racchetta(Bordo.BASSO, CAMPO);
        assertEquals(500 - 30 - 12, basso.getY(), 1e-9);
        assertEquals(Bordo.BASSO, basso.getBordo());
    }

    @Test
    void unaRacchettaOrizzontaleSiMuoveSoloInOrizzontale() {
        Racchetta r = new Racchetta(Bordo.ALTO, CAMPO);
        r.muovi(Mossa.SU, CAMPO);
        assertEquals(360 - Racchetta.VELOCITA, r.getX());
        assertEquals(30, r.getY(), 1e-9);
        r.muovi(Mossa.GIU, CAMPO);
        r.muovi(Mossa.GIU, CAMPO);
        assertEquals(360 + Racchetta.VELOCITA, r.getX());
        assertEquals(30, r.getY(), 1e-9);
    }

    @Test
    void unaRacchettaOrizzontaleNonEsceDalCampo() {
        Racchetta r = new Racchetta(Bordo.BASSO, CAMPO);
        for (int i = 0; i < 200; i++) r.muovi(Mossa.GIU, CAMPO);
        assertEquals(800 - 80, r.getX());
        for (int i = 0; i < 200; i++) r.muovi(Mossa.SU, CAMPO);
        assertEquals(0, r.getX());
    }

    @Test
    void ilCostruttoreDellaRacchettaRifiutaNull() {
        assertThrows(IllegalArgumentException.class, () -> new Racchetta(null, CAMPO));
        assertThrows(IllegalArgumentException.class, () -> new Racchetta(Bordo.ALTO, null));
    }

    // ---- i livelli

    @Test
    void iBordiDeiCinqueLivelli() {
        Bordo[][] attesi = {
            {Bordo.SINISTRA, Bordo.DESTRA}, {Bordo.BASSO, Bordo.ALTO}, {Bordo.SINISTRA, Bordo.ALTO},
            {Bordo.BASSO, Bordo.DESTRA}, {Bordo.ALTO, Bordo.SINISTRA}};
        for (int numero = 1; numero <= 5; numero++) {
            Livello l = MotoreDeiLivelli.livello(numero, 800, 500);
            assertEquals(attesi[numero - 1][0], l.bordoUmano(), "livello " + numero);
            assertEquals(attesi[numero - 1][1], l.bordoAutomatico(), "livello " + numero);
        }
    }

    @Test
    void iDueGiocatoriNonSonoMaiSulloStessoBordo() {
        for (int numero = 1; numero <= 5; numero++) {
            Livello l = MotoreDeiLivelli.livello(numero, 800, 500);
            assertNotEquals(l.bordoUmano(), l.bordoAutomatico(), "livello " + numero);
        }
    }

    @Test
    void laPartitaMetteLeRacchetteSuiBordiDelLivello() {
        Partita p = new Partita(CAMPO, FERMO);
        p.preparaLivello(4);                  // la persona in basso, l'automatico a destra
        assertEquals(Bordo.BASSO, p.getRacchettaUmana().getBordo());
        assertEquals(Bordo.DESTRA, p.getRacchettaAutomatica().getBordo());
        p.preparaLivello(2);                  // in basso e in alto
        assertEquals(Bordo.BASSO, p.getRacchettaUmana().getBordo());
        assertEquals(Bordo.ALTO, p.getRacchettaAutomatica().getBordo());
    }

    @Test
    void doveNonGiocaNessunoCeIlMuro() {
        Partita p = new Partita(CAMPO, FERMO);
        p.preparaLivello(3);                  // la persona a sinistra, l'automatico in alto
        assertTrue(p.isMuro(Bordo.DESTRA));
        assertTrue(p.isMuro(Bordo.BASSO));
        assertFalse(p.isMuro(Bordo.SINISTRA));
        assertFalse(p.isMuro(Bordo.ALTO));
    }

    // ---- la palla

    @Test
    void laPallaLanciataVersoUnBordoVaVersoQuelBordo() {
        Palla palla = new Palla(400, 250);
        palla.lancia(400, 250, Bordo.ALTO, 5);
        assertEquals(-5, palla.getVelocitaY(), 1e-9);
        palla.lancia(400, 250, Bordo.BASSO, 5);
        assertEquals(5, palla.getVelocitaY(), 1e-9);
        palla.lancia(400, 250, Bordo.SINISTRA, 5);
        assertEquals(-5, palla.getVelocitaX(), 1e-9);
    }

    @Test
    void sullaRacchettaOrizzontaleLaPallaTornaIndietroInVerticale() {
        Racchetta alto = new Racchetta(Bordo.ALTO, CAMPO);
        Palla palla = new Palla(400, 250);
        palla.lancia(400, 50, Bordo.ALTO, 5);
        palla.rimbalzaSu(alto);
        assertTrue(palla.getVelocitaY() > 0, "dopo la racchetta in alto la palla deve scendere");
    }

    @Test
    void presaQuasiDiLatoLaPallaNonRallenta() {
        // la palla va verso destra, quasi in orizzontale, scendendo un poco, e prende la racchetta in basso:
        // dopo il colpo deve allontanarsi dalla racchetta almeno alla velocità del lancio
        Racchetta basso = new Racchetta(Bordo.BASSO, CAMPO);
        Palla palla = new Palla(400, 250);
        do {
            palla.lancia(400, 455, Bordo.DESTRA, 5);  // l'angolo del lancio è a caso: si rilancia finché scende
        } while (palla.getVelocitaY() <= 0);
        palla.rimbalzaSu(basso);
        assertTrue(palla.getVelocitaY() <= -5 + 1e-9, "velocità verticale dopo il colpo: " + palla.getVelocitaY());
    }

    // ---- muri e punti, nella partita

    @Test
    void sulMuroLaPallaRimbalzaESenzaPunti() {
        Partita p = partitaAlLivello(3);      // a destra c'è il muro
        p.getPalla().lancia(740, 250, Bordo.DESTRA, 5);
        aggiorna(p, 30);
        assertTrue(p.getPalla().getVelocitaX() < 0, "dopo il muro a destra la palla deve tornare verso sinistra");
        assertEquals(0, p.getPunti(Lato.SINISTRA));
        assertEquals(0, p.getPunti(Lato.DESTRA));
    }

    @Test
    void sulMuroInBassoLaPallaRimbalza() {
        Partita p = partitaAlLivello(3);      // in basso c'è il muro
        p.getPalla().lancia(650, 460, Bordo.BASSO, 5);
        aggiorna(p, 20);
        assertTrue(p.getPalla().getVelocitaY() < 0, "dopo il muro in basso la palla deve risalire");
        assertEquals(0, p.getPunti(Lato.SINISTRA) + p.getPunti(Lato.DESTRA));
    }

    @Test
    void seLaPallaEsceDalBordoDellAutomaticoIlPuntoEDellaPersona() {
        Partita p = partitaAlLivello(3);      // l'automatico in alto
        p.getPalla().lancia(650, 12, Bordo.ALTO, 5);
        aggiorna(p, 10);
        assertEquals(1, p.getPunti(Lato.SINISTRA));
        assertEquals(0, p.getPunti(Lato.DESTRA));
    }

    @Test
    void seLaPallaEsceDalBordoDellaPersonaIlPuntoEDellAutomatico() {
        Partita p = partitaAlLivello(4);      // la persona in basso
        p.getPalla().lancia(650, 490, Bordo.BASSO, 5);
        aggiorna(p, 10);
        assertEquals(1, p.getPunti(Lato.DESTRA));
        assertEquals(0, p.getPunti(Lato.SINISTRA));
    }
}
