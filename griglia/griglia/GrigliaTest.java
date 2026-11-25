// Ultimate Tris — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 6: i test ufficiali della Griglia<T>.
package griglia;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class GrigliaTest {

    // ------------------------------------------------------------ costruttore e dimensioni

    @Test
    void unaGrigliaNuovaHaLeDimensioniDate() {
        Griglia<String> g = new Griglia<>(3, 4);
        assertEquals(3, g.getRighe());
        assertEquals(4, g.getColonne());
    }

    @Test
    void unaGrigliaNuovaÈTuttaVuota() {
        Griglia<String> g = new Griglia<>(3, 4);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                assertTrue(g.isVuota(r, c), "casella (" + r + ", " + c + ")");
                assertNull(g.get(r, c), "casella (" + r + ", " + c + ")");
            }
        }
        assertFalse(g.isPiena());
    }

    // new Griglia<String>(…): il tipo fra le parentesi angolari va scritto per esteso dopo new, perché qui non c'è
    // una variabile dichiarata a sinistra da cui Java possa ricavarlo (con una variabile basta il diamante, new Griglia<>(…)).

    @Test
    void zeroRigheLancia() {
        assertThrows(IllegalArgumentException.class, () -> new Griglia<String>(0, 3));
    }

    @Test
    void colonneNegativeLancia() {
        assertThrows(IllegalArgumentException.class, () -> new Griglia<String>(3, -1));
    }

    @Test
    void righeNegativeLancia() {
        assertThrows(IllegalArgumentException.class, () -> new Griglia<String>(-1, 3));
    }

    @Test
    void zeroColonneLancia() {
        assertThrows(IllegalArgumentException.class, () -> new Griglia<String>(3, 0));
    }

    @Test
    void unaRigaEUnaColonnaVanno() {
        Griglia<String> g = new Griglia<>(1, 1);
        g.metti(0, 0, "a");
        assertEquals("a", g.get(0, 0));
        assertTrue(g.isPiena());
    }

    @Test
    void isDentroAiBordi() {
        Griglia<String> g = new Griglia<>(3, 4);
        assertTrue(g.isDentro(0, 0));
        assertTrue(g.isDentro(2, 3));
        assertFalse(g.isDentro(-1, 0));
        assertFalse(g.isDentro(0, -1));
        assertFalse(g.isDentro(3, 0));
        assertFalse(g.isDentro(0, 4));
    }

    // ------------------------------------------------------------ get, metti, svuota

    @Test
    void mettiPoiGet() {
        Griglia<String> g = new Griglia<>(3, 4);
        g.metti(1, 2, "a");
        assertEquals("a", g.get(1, 2));
        assertFalse(g.isVuota(1, 2));
    }

    @Test
    void mettiSostituisce() {
        Griglia<String> g = new Griglia<>(3, 3);
        g.metti(0, 0, "a");
        g.metti(0, 0, "b");
        assertEquals("b", g.get(0, 0));
    }

    @Test
    void mettiNonToccaLeAltreCaselle() {
        Griglia<String> g = new Griglia<>(3, 4);
        g.metti(1, 2, "a");
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                if (r != 1 || c != 2) assertTrue(g.isVuota(r, c), "casella (" + r + ", " + c + ")");
            }
        }
    }

    @Test
    void mettiFuoriLanciaENonCambiaNiente() {
        Griglia<String> g = new Griglia<>(3, 4);
        assertThrows(IllegalArgumentException.class, () -> g.metti(3, 0, "a"));
        assertThrows(IllegalArgumentException.class, () -> g.metti(0, 4, "a"));
        assertThrows(IllegalArgumentException.class, () -> g.metti(-1, 0, "a"));
        assertThrows(IllegalArgumentException.class, () -> g.metti(1, -1, "a"));
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) assertTrue(g.isVuota(r, c));
        }
    }

    @Test
    void mettiNullLanciaENonCambiaNiente() {
        Griglia<String> g = new Griglia<>(3, 3);
        g.metti(1, 1, "a");
        assertThrows(IllegalArgumentException.class, () -> g.metti(1, 1, null));
        assertEquals("a", g.get(1, 1));
    }

    @Test
    void getFuoriLancia() {
        Griglia<String> g = new Griglia<>(3, 4);
        assertThrows(IllegalArgumentException.class, () -> g.get(3, 0));
        assertThrows(IllegalArgumentException.class, () -> g.get(0, 4));
        assertThrows(IllegalArgumentException.class, () -> g.get(0, -1));
        assertThrows(IllegalArgumentException.class, () -> g.get(-1, 0));
    }

    @Test
    void svuotaRendeVuota() {
        Griglia<String> g = new Griglia<>(3, 3);
        g.metti(2, 1, "a");
        g.svuota(2, 1);
        assertTrue(g.isVuota(2, 1));
        assertNull(g.get(2, 1));
    }

    @Test
    void svuotaUnaVuotaNonCambiaNiente() {
        Griglia<String> g = new Griglia<>(2, 2);
        g.metti(0, 0, "a");
        g.svuota(1, 1);
        assertTrue(g.isVuota(1, 1));
        assertEquals("a", g.get(0, 0));
    }

    @Test
    void svuotaEIsVuotaFuoriLanciano() {
        Griglia<String> g = new Griglia<>(3, 4);
        assertThrows(IllegalArgumentException.class, () -> g.svuota(0, 4));
        assertThrows(IllegalArgumentException.class, () -> g.isVuota(3, 0));
    }

    @Test
    void unaGrigliaDiInteri() {
        Griglia<Integer> g = new Griglia<>(2, 3);
        g.metti(1, 2, 1000);
        int x = g.get(1, 2);
        assertEquals(1000, x);
    }

    @Test
    void mettereDueVolteNellaStessaCasellaNonRiempieLeAltre() {
        Griglia<String> g = new Griglia<>(2, 2);
        g.metti(0, 0, "a");
        g.metti(0, 0, "b");
        g.metti(0, 1, "c");
        g.metti(1, 0, "d");
        assertFalse(g.isPiena());
    }

    @Test
    void svuotareDueVolteNonCambiaIsPiena() {
        Griglia<String> g = new Griglia<>(2, 2);
        g.metti(0, 0, "a");
        g.metti(0, 1, "b");
        g.metti(1, 0, "c");
        g.metti(1, 1, "d");
        g.svuota(1, 1);
        g.svuota(1, 1);
        g.metti(1, 1, "e");
        assertTrue(g.isPiena());
    }

    @Test
    void isPienaSoloQuandoSonoPieneTutte() {
        Griglia<String> g = new Griglia<>(2, 2);
        g.metti(0, 0, "a");
        g.metti(0, 1, "b");
        g.metti(1, 0, "c");
        assertFalse(g.isPiena());
        g.metti(1, 1, "d");
        assertTrue(g.isPiena());
        g.svuota(0, 1);
        assertFalse(g.isPiena());
    }

    // ------------------------------------------------------------ riga, colonna, linee

    /** Una griglia 3 × 4 con le lettere da «a» a «l», riga per riga; la casella (1, 1) è vuota. */
    static Griglia<String> lettere() {
        Griglia<String> g = new Griglia<>(3, 4);
        String[] s = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"};
        for (int i = 0; i < 12; i++) g.metti(i / 4, i % 4, s[i]);
        g.svuota(1, 1);
        return g;
    }

    // java.util.Arrays.asList(...): una lista con i valori dati; a differenza di List.of accetta null.

    @Test
    void rigaDaSinistraADestraConNullPerLeVuote() {
        assertEquals(List.of("a", "b", "c", "d"), lettere().riga(0));
        assertEquals(java.util.Arrays.asList("e", null, "g", "h"), lettere().riga(1));
    }

    @Test
    void colonnaDallAltoInBassoConNullPerLeVuote() {
        assertEquals(java.util.Arrays.asList("b", null, "j"), lettere().colonna(1));
        assertEquals(List.of("d", "h", "l"), lettere().colonna(3));
    }

    @Test
    void rigaEColonnaFuoriLanciano() {
        Griglia<String> g = lettere();
        assertThrows(IllegalArgumentException.class, () -> g.riga(3));
        assertThrows(IllegalArgumentException.class, () -> g.colonna(4));
        assertThrows(IllegalArgumentException.class, () -> g.riga(-1));
        assertThrows(IllegalArgumentException.class, () -> g.colonna(-1));
    }

    @Test
    void cambiareLaListaDiUnaRigaNonCambiaLaGriglia() {
        Griglia<String> g = lettere();
        List<String> r = g.riga(0);
        try {
            r.set(0, "z");
        } catch (UnsupportedOperationException e) {
            // va bene anche una lista che non si lascia cambiare
        }
        assertEquals("a", g.get(0, 0));
    }

    @Test
    void cambiareLaListaDiUnaColonnaNonCambiaLaGriglia() {
        Griglia<String> g = lettere();
        List<String> c = g.colonna(0);
        try {
            c.set(1, "z");
        } catch (UnsupportedOperationException e) {
            // va bene anche una lista che non si lascia cambiare
        }
        assertEquals("e", g.get(1, 0));
    }

    @Test
    void laListaDiUnaRigaNonCambiaSePoiCambiaLaGriglia() {
        Griglia<String> g = lettere();
        List<String> r = g.riga(0);
        g.metti(0, 0, "z");
        assertEquals("a", r.get(0));
    }

    @Test
    void laListaDiUnaColonnaNonCambiaSePoiCambiaLaGriglia() {
        Griglia<String> g = lettere();
        List<String> c = g.colonna(0);
        g.metti(1, 0, "z");
        assertEquals("e", c.get(1));
    }

    @Test
    void leLineeNonCambianoSePoiCambiaLaGriglia() {
        Griglia<String> g = lettere();
        List<List<String>> linee = g.linee();
        g.metti(0, 0, "z");
        assertEquals("a", linee.get(0).get(0));
        assertEquals("a", linee.get(3).get(0));
    }

    @Test
    void lineeDiUnaTreXTre() {
        Griglia<String> g = new Griglia<>(3, 3);
        String[] s = {"a", "b", "c", "d", "e", "f", "g", "h", "i"};
        for (int i = 0; i < 9; i++) g.metti(i / 3, i % 3, s[i]);
        List<List<String>> attese = List.of(
                List.of("a", "b", "c"), List.of("d", "e", "f"), List.of("g", "h", "i"),
                List.of("a", "d", "g"), List.of("b", "e", "h"), List.of("c", "f", "i"),
                List.of("a", "e", "i"), List.of("c", "e", "g"));
        assertEquals(attese, g.linee());
    }

    @Test
    void unaGrigliaNonQuadrataNonHaDiagonali() {
        List<List<String>> linee = lettere().linee();
        assertEquals(7, linee.size());
        assertEquals(List.of("a", "b", "c", "d"), linee.get(0));
        assertEquals(List.of("d", "h", "l"), linee.get(6));
    }

    @Test
    void leLineeHannoNullPerLeVuote() {
        Griglia<String> g = new Griglia<>(2, 2);
        g.metti(0, 0, "a");
        List<List<String>> linee = g.linee();
        assertEquals(6, linee.size());
        assertEquals(java.util.Arrays.asList("a", null), linee.get(0));
        assertEquals(java.util.Arrays.asList(null, null), linee.get(1));
        assertEquals(java.util.Arrays.asList("a", null), linee.get(4));
        assertEquals(java.util.Arrays.asList(null, null), linee.get(5));
    }

    // ------------------------------------------------------------ tris

    /**
     * Una griglia 3 × 3 con le parole date, riga per riga; null lascia la casella vuota.
     * String... parole: si passano quante parole si vuole, e dentro il metodo sono un array.
     */
    static Griglia<String> treXTre(String... parole) {
        Griglia<String> g = new Griglia<>(3, 3);
        for (int i = 0; i < 9; i++) {
            if (parole[i] != null) g.metti(i / 3, i % 3, parole[i]);
        }
        return g;
    }

    @Test
    void trisSuUnaRiga() {
        Griglia<String> g = treXTre("O", null, "O", "X", "X", "X", null, "O", null);
        assertEquals("X", g.tris());
    }

    @Test
    void trisSuUnaColonna() {
        Griglia<String> g = treXTre("X", "O", null, "X", "O", null, null, "O", "X");
        assertEquals("O", g.tris());
    }

    @Test
    void trisSulleDueDiagonali() {
        assertEquals("X", treXTre("X", "O", null, "O", "X", null, null, null, "X").tris());
        assertEquals("O", treXTre("X", "X", "O", null, "O", null, "O", null, "X").tris());
    }

    @Test
    void senzaTrisNull() {
        Griglia<String> g = treXTre("X", "O", "X", "X", "O", "O", "O", "X", "X");
        assertNull(g.tris());
    }

    @Test
    void leCaselleVuoteNonFannoTris() {
        assertNull(new Griglia<String>(3, 3).tris());
        assertNull(treXTre("X", "X", null, null, null, null, null, null, null).tris());
    }

    @Test
    void unaCasellaVuotaInMezzoInterrompeLaLinea() {
        // la riga 0 ha una casella vuota in mezzo: non è un tris, anche se le altre due sono uguali
        Griglia<String> g = treXTre("X", null, "X", "O", null, null, null, null, null);
        assertNull(g.tris());
    }

    @Test
    void trisConfrontaConEquals() {
        // tre stringhe uguali ma tre oggetti diversi (new String crea ogni volta un oggetto nuovo)
        Griglia<String> g = treXTre(null, null, null, new String("ab"), new String("ab"), new String("ab"), null, null, null);
        assertEquals("ab", g.tris());
    }

    /**
     * Usata come valore nel test sotto: due Nome con le stesse lettere sono uguali per equals, come chiede il
     * contratto di tris. La classe non ridefinisce hashCode, apposta: hashCode è il numero che usano HashSet e
     * HashMap per trovare un oggetto, e quello di Object è diverso per quasi ogni oggetto.
     */
    static class Nome {
        private final String lettere;

        Nome(String lettere) {
            this.lettere = lettere;
        }

        @Override
        public boolean equals(Object altro) {
            if (!(altro instanceof Nome)) return false;
            return lettere.equals(((Nome) altro).lettere);
        }

        @Override
        public String toString() {
            return "Nome " + lettere;
        }
    }

    @Test
    void trisConfrontaConEqualsAncheSenzaHashCode() {
        // tre oggetti Nome diversi, ma uguali per equals. Un tris che li mette in un HashSet ne trova tre, non uno:
        // deve confrontarli con equals.
        Griglia<Nome> g = new Griglia<>(3, 3);
        for (int c = 0; c < 3; c++) g.metti(1, c, new Nome("X"));
        assertEquals(new Nome("X"), g.tris(), "tre valori uguali per equals fanno tris");
    }

    @Test
    void trisSuUnaGrigliaDiInteri() {
        // 1000 è fuori dai numeri che Java tiene pronti (da -128 a 127): ogni 1000 qui è un oggetto Integer nuovo
        Griglia<Integer> g = new Griglia<>(3, 3);
        for (int r = 0; r < 3; r++) g.metti(r, 2, 1000);
        g.metti(0, 0, 7);
        assertEquals(1000, g.tris());
    }

    @Test
    void trisDàLaPrimaLineaNellOrdineDiLinee() {
        // due righe piene: conta la prima nell'ordine di linee()
        Griglia<String> g = treXTre("X", "X", "X", null, null, null, "O", "O", "O");
        assertEquals("X", g.tris());
    }

    @Test
    void inUnaGrigliaNonQuadrataServeTuttaLaRiga() {
        // tre X in fila non bastano: la riga ha quattro caselle
        Griglia<String> g = new Griglia<>(3, 4);
        g.metti(0, 0, "X");
        g.metti(0, 1, "X");
        g.metti(0, 2, "X");
        assertNull(g.tris());
        g.metti(0, 3, "X");
        assertEquals("X", g.tris());
    }

    @Test
    void inUnaQuattroPerQuattroServeTuttaLaDiagonale() {
        Griglia<String> g = new Griglia<>(4, 4);
        for (int i = 0; i < 3; i++) g.metti(i, i, "O");
        assertNull(g.tris());
        g.metti(3, 3, "O");
        assertEquals("O", g.tris());
    }

    @Test
    void unaGrigliaUnoPerUno() {
        // una casella sola: è una riga, una colonna e le due diagonali
        Griglia<String> g = new Griglia<>(1, 1);
        assertNull(g.tris());
        g.metti(0, 0, "X");
        assertEquals("X", g.tris());
    }

    @Test
    void trisNonCambiaLaGriglia() {
        Griglia<String> g = treXTre("X", "X", "X", "O", "O", null, null, null, null);
        String prima = g.toString();
        g.tris();
        assertEquals(prima, g.toString());
    }

    @Test
    void inUnaGrigliaNonQuadrataLeDiagonaliNonContano() {
        Griglia<String> g = new Griglia<>(3, 4);
        g.metti(0, 0, "X");
        g.metti(1, 1, "X");
        g.metti(2, 2, "X");
        assertNull(g.tris());
    }
}
