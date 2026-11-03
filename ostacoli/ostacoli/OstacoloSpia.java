// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 4: un aiuto per i test. Non è un ostacolo del gioco.
package ostacoli;

import geometria.Rettangolo;
import java.awt.Color;
import java.util.List;

/**
 * Un ostacolo finto, scritto per i test: si rompe al primo colpo, e in più conta quante volte viene
 * chiamato colpisci (anche quando è già rotto, prima di lanciare l'eccezione) e scrive il proprio
 * nome in un registro comune, per vedere in che ordine gli ostacoli sono colpiti.
 */
final class OstacoloSpia implements Ostacolo {
    private final Rettangolo area;
    private final String nome;
    private final List<String> registro;
    private boolean rotto;
    private int chiamate;

    OstacoloSpia(Rettangolo area, String nome, List<String> registro) {
        this.area = area;
        this.nome = nome;
        this.registro = registro;
    }

    /** Quante volte è stato chiamato colpisci, anche da rotto. */
    int getChiamate() { return chiamate; }

    /** Lo rompe senza passare da colpisci (e senza contare), per preparare un caso di prova. */
    void rompiPrima() { rotto = true; }

    @Override public Rettangolo getArea() { return area; }

    @Override
    public void colpisci(Ostacoli campo) {
        chiamate++;
        registro.add(nome);
        if (rotto) throw new IllegalStateException("la spia " + nome + " è già rotta");
        rotto = true;
    }

    @Override public boolean isRotto() { return rotto; }
    @Override public Color getColore() { return Color.WHITE; }
    @Override public void avanza() { }
}
