// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 4: un aiuto per i test.
package ostacoli;

import geometria.Rettangolo;

/**
 * Le aree degli ostacoli nei test: una griglia di ostacoli 20 x 40, con 30 pixel di spazio fra l'uno
 * e l'altro, in orizzontale e in verticale. 30 è proprio OstacoloEsplosivo.PORTATA: l'esplosione
 * di un ostacolo della griglia arriva, giusta giusta, agli otto che ha attorno.
 */
final class Celle {
    private Celle() { }

    /** L'area dell'ostacolo nella riga e nella colonna date, contando da 0 in alto a sinistra. */
    static Rettangolo cella(int riga, int colonna) {
        return new Rettangolo(colonna * 50, riga * 70, 20, 40);
    }
}
