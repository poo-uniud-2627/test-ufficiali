// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 5: la scelta della variante, controllata da un test.
package lab2;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Nella cartella principale del repository c'è il file VARIANTE.txt, con un numero solo: 1, 2 o 3.
 * Il numero dice quale variante realizzate. GitHub lo legge e scarica i test ufficiali di quella variante.
 */
class SceltaDellaVarianteTest {

    @Test
    void ilFileVarianteDiceQualeVarianteRealizzate() throws Exception {
        Path file = Path.of("VARIANTE.txt");
        assertTrue(Files.exists(file), "manca il file VARIANTE.txt nella cartella principale del repository");
        String scelta = Files.readString(file, StandardCharsets.UTF_8).strip();
        assertTrue(scelta.equals("1") || scelta.equals("2") || scelta.equals("3"),
            "VARIANTE.txt deve contenere solo 1, 2 o 3; contiene: «" + scelta + "»");
    }
}
