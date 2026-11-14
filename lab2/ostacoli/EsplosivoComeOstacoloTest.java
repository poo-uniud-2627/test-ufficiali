// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Lezione 12 e consegna 5: l'esplosivo.
package ostacoli;

import geometria.Rettangolo;

class EsplosivoComeOstacoloTest extends ContrattoOstacoloTest {
    @Override
    Ostacolo nuovoOstacolo(Rettangolo area) {
        return new OstacoloEsplosivo(area);
    }
}
