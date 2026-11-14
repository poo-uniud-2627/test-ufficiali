// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 5, variante 2 (le stelle): i test ufficiali della classe Livelli.
// Sono i test della consegna 3, più quelli di guadagnaVita. Ogni test è un caso del contratto.
package pong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LivelliTest {

    // ---- Livelli: il costruttore

    @Test
    void unGiocoNuovoCominciaDalLivello1ConTutteLeVite() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        assertEquals(1, l.getLivello());
        assertEquals(3, l.getVite());
        assertEquals(5, l.getNumeroLivelli());
        assertEquals(Lato.SINISTRA, l.getUmano());
        assertFalse(l.isCompletato());
        assertFalse(l.isPerso());
        assertFalse(l.isFinito());
    }

    @Test
    void ilCostruttoreDiLivelliRifiutaZeroLivelliOZeroVite() {
        assertThrows(IllegalArgumentException.class, () -> new Livelli(0, 3, Lato.SINISTRA));
        assertThrows(IllegalArgumentException.class, () -> new Livelli(5, 0, Lato.SINISTRA));
        assertThrows(IllegalArgumentException.class, () -> new Livelli(-1, -1, Lato.SINISTRA));
    }

    @Test
    void ilCostruttoreDiLivelliRifiutaUmanoNull() {
        assertThrows(IllegalArgumentException.class, () -> new Livelli(5, 3, null));
    }

    @Test
    void ilCostruttoreDiLivelliAccettaIConfini() {
        Livelli l = new Livelli(1, 1, Lato.DESTRA);
        assertEquals(1, l.getLivello());
        assertEquals(1, l.getVite());
        assertFalse(l.isFinito());
    }

    // ---- Livelli: vincere

    @Test
    void vincendoSiPassaAlLivelloDopoELeViteNonCambiano() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(2, l.getLivello());
        assertEquals(3, l.getVite());
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(3, l.getLivello());
        assertFalse(l.isFinito());
    }

    @Test
    void vincereNonRestituisceLeVitePerse() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(2, l.getLivello());
        assertEquals(2, l.getVite());
    }

    @Test
    void contaIlLatoDellUmanoNonLaSinistra() {
        Livelli l = new Livelli(5, 3, Lato.DESTRA);
        l.partitaFinita(Lato.DESTRA);
        assertEquals(2, l.getLivello());
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(2, l.getLivello());
        assertEquals(2, l.getVite());
    }

    @Test
    void vintoLUltimoLivelloIlGiocoECompletatoEIlLivelloRestaLUltimo() {
        Livelli l = new Livelli(3, 3, Lato.SINISTRA);
        vinci(l, 3);
        assertTrue(l.isCompletato());
        assertTrue(l.isFinito());
        assertFalse(l.isPerso());
        assertEquals(3, l.getLivello());
        assertEquals(3, l.getVite());
    }

    @Test
    void conUnSoloLivelloBastaUnaVittoria() {
        Livelli l = new Livelli(1, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        assertTrue(l.isCompletato());
        assertEquals(1, l.getLivello());
    }

    // ---- Livelli: perdere

    @Test
    void perdendoSiPerdeUnaVitaEIlLivelloNonCambia() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertEquals(2, l.getLivello());
        assertEquals(2, l.getVite());
        assertFalse(l.isFinito());
    }

    @Test
    void conLUltimaVitaIlGiocoEPerso() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        perdi(l, 3);
        assertEquals(0, l.getVite());
        assertTrue(l.isPerso());
        assertTrue(l.isFinito());
        assertFalse(l.isCompletato());
        assertEquals(1, l.getLivello());
    }

    @Test
    void perdereLUltimaVitaAllUltimoLivelloEPersoNonCompletato() {
        Livelli l = new Livelli(2, 1, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertTrue(l.isPerso());
        assertFalse(l.isCompletato());
        assertEquals(2, l.getLivello());
    }

    // ---- Livelli: a gioco finito non cambia niente

    @Test
    void aGiocoCompletatoNonCambiaNiente() {
        Livelli l = new Livelli(2, 3, Lato.SINISTRA);
        vinci(l, 2);
        l.partitaFinita(Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertTrue(l.isCompletato());
        assertFalse(l.isPerso());
        assertEquals(2, l.getLivello());
        assertEquals(3, l.getVite());
    }

    @Test
    void aGiocoPersoNonCambiaNiente() {
        Livelli l = new Livelli(5, 2, Lato.SINISTRA);
        perdi(l, 2);
        l.partitaFinita(Lato.DESTRA);
        assertEquals(0, l.getVite());
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(1, l.getLivello());
        assertTrue(l.isPerso());
        assertFalse(l.isCompletato());
    }

    @Test
    void partitaFinitaConNullLanciaENonCambiaNiente() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertThrows(IllegalArgumentException.class, () -> l.partitaFinita(null));
        assertEquals(2, l.getLivello());
        assertEquals(2, l.getVite());
    }

    @Test
    void partitaFinitaConNullLanciaAncheAGiocoFinito() {
        Livelli l = new Livelli(1, 1, Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertThrows(IllegalArgumentException.class, () -> l.partitaFinita(null));
    }

    // ---- Livelli: ricominciare

    @Test
    void ricominciaRiportaAlLivello1ConTutteLeVite() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        vinci(l, 2);
        perdi(l, 1);
        l.ricomincia();
        assertEquals(1, l.getLivello());
        assertEquals(3, l.getVite());
        assertFalse(l.isFinito());
    }

    @Test
    void ricominciaDopoUnGiocoCompletatoOPerso() {
        Livelli vinto = new Livelli(2, 3, Lato.SINISTRA);
        vinci(vinto, 2);
        vinto.ricomincia();
        assertFalse(vinto.isCompletato());
        vinto.partitaFinita(Lato.SINISTRA);
        assertEquals(2, vinto.getLivello());

        Livelli perso = new Livelli(2, 2, Lato.SINISTRA);
        perdi(perso, 2);
        perso.ricomincia();
        assertFalse(perso.isPerso());
        assertEquals(2, perso.getVite());
    }

    // ---- Livelli: riguadagnare una vita (variante 2)

    @Test
    void guadagnaVitaRestituisceUnaVitaPersa() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        perdi(l, 2);
        l.guadagnaVita();
        assertEquals(2, l.getVite());
        assertEquals(1, l.getLivello());
    }

    @Test
    void guadagnaVitaNonVaOltreLeViteIniziali() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        l.guadagnaVita();
        assertEquals(3, l.getVite());
        perdi(l, 1);
        l.guadagnaVita();
        l.guadagnaVita();
        assertEquals(3, l.getVite());
    }

    @Test
    void guadagnaVitaNonCambiaIlLivello() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        vinci(l, 2);
        perdi(l, 1);
        l.guadagnaVita();
        assertEquals(3, l.getLivello());
        assertEquals(3, l.getVite());
    }

    @Test
    void aGiocoPersoGuadagnaVitaNonCambiaNiente() {
        Livelli l = new Livelli(5, 2, Lato.SINISTRA);
        perdi(l, 2);
        l.guadagnaVita();
        assertEquals(0, l.getVite());
        assertTrue(l.isPerso());
    }

    @Test
    void aGiocoCompletatoGuadagnaVitaNonCambiaNiente() {
        Livelli l = new Livelli(2, 3, Lato.SINISTRA);
        perdi(l, 1);
        vinci(l, 2);
        l.guadagnaVita();
        assertEquals(2, l.getVite());
        assertTrue(l.isCompletato());
    }

    @Test
    void unaVitaRiguadagnataSiPuoPerdereDiNuovo() {
        Livelli l = new Livelli(5, 1, Lato.SINISTRA);
        l.guadagnaVita();
        assertEquals(1, l.getVite());
        perdi(l, 1);
        assertTrue(l.isPerso());
    }

    // ---- attrezzi per i livelli

    private static void vinci(Livelli l, int volte) {
        for (int i = 0; i < volte; i++) l.partitaFinita(l.getUmano());
    }

    private static void perdi(Livelli l, int volte) {
        Lato altro = l.getUmano() == Lato.SINISTRA ? Lato.DESTRA : Lato.SINISTRA;
        for (int i = 0; i < volte; i++) l.partitaFinita(altro);
    }
}
