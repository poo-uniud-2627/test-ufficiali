// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Lezione 12 e consegna 5: il fragile da due colpi.
package ostacoli;

import geometria.Rettangolo;

class FragileComeOstacoloTest extends ContrattoOstacoloTest {
    @Override
    Ostacolo nuovoOstacolo(Rettangolo area) {
        return new OstacoloFragile(area, 2);
    }
}
