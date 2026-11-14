// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 5: il fisso con le promesse di Ostacolo.
package ostacoli;

import geometria.Rettangolo;

class FissoComeOstacoloTest extends ContrattoOstacoloTest {
    @Override
    Ostacolo nuovoOstacolo(Rettangolo area) {
        return new OstacoloFisso(area);
    }
}
