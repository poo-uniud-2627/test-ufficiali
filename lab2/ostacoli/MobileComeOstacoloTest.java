// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Lezione 12 (soluzione del fate voi) e consegna 5: il mobile con le promesse di Ostacolo.
package ostacoli;

import geometria.Rettangolo;

class MobileComeOstacoloTest extends ContrattoOstacoloTest {
    @Override
    Ostacolo nuovoOstacolo(Rettangolo area) {
        return new OstacoloMobile(area, 2, 0, 500);   // 2 pixel per passo, fra y = 0 e y = 500
    }
}
