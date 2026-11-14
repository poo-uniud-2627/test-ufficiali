// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 5: la regola «ogni ostacolo ha il suo test del contratto», controllata da un test.
package ostacoli;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Una regola della consegna 5, controllata da un test.
 *
 * Ogni classe in src/ che realizza Ostacolo, e che si può creare con new,
 * deve avere una sottoclasse di ContrattoOstacoloTest che la prova.
 * Le classi astratte e le interfacce non contano.
 *
 * Il test cerca le sottoclassi fra le classi di test di questo pacchetto
 * e chiama il loro nuovoOstacolo: conta la classe dell'oggetto creato.
 */
class OgniOstacoloHaIlTestDelContrattoTest {

    private static final Path SRC = Path.of("src");

    @Test
    void ogniOstacoloInSrcHaLaSuaSottoclasseDelTestDelContratto() throws Exception {
        assertTrue(Files.isDirectory(SRC),
            "non trovo la cartella src: lanciate i test dalla cartella principale del repository");

        Set<String> ostacoliInSrc = new TreeSet<>();
        for (String nome : classiIn(SRC)) {
            Class<?> c = carica(nome);
            if (c != null && Ostacolo.class.isAssignableFrom(c)
                    && !c.isInterface() && !Modifier.isAbstract(c.getModifiers())) {
                ostacoliInSrc.add(c.getName());
            }
        }

        Set<String> provati = new TreeSet<>();
        for (Class<?> t : sottoclassiDelTestDelContratto()) {
            Constructor<?> costruttore = t.getDeclaredConstructor();
            costruttore.setAccessible(true);
            ContrattoOstacoloTest test = (ContrattoOstacoloTest) costruttore.newInstance();
            Ostacolo o = test.nuovoOstacolo(Celle.cella(1, 1));
            assertNotNull(o, t.getSimpleName() + ": nuovoOstacolo ha restituito null");
            provati.add(o.getClass().getName());
        }

        Set<String> senzaTest = new TreeSet<>(ostacoliInSrc);
        senzaTest.removeAll(provati);
        assertTrue(senzaTest.isEmpty(),
            "Queste classi realizzano Ostacolo ma nessun test del contratto le prova: " + senzaTest
            + ". Per ciascuna scrivete in test/ostacoli una sottoclasse di ContrattoOstacoloTest,"
            + " come FragileComeOstacoloTest.");
    }

    /** I nomi completi delle classi dei file .java sotto la cartella data. */
    private static List<String> classiIn(Path radice) throws IOException {
        List<String> nomi = new ArrayList<>();
        try (Stream<Path> file = Files.walk(radice)) {
            file.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                String relativo = radice.relativize(p).toString();
                relativo = relativo.substring(0, relativo.length() - ".java".length());
                nomi.add(relativo.replace('/', '.').replace('\\', '.'));
            });
        }
        return nomi;
    }

    /** Le sottoclassi concrete di ContrattoOstacoloTest fra le classi compilate di questo pacchetto. */
    private static List<Class<?>> sottoclassiDelTestDelContratto() throws Exception {
        List<Class<?>> trovate = new ArrayList<>();
        Set<String> visti = new TreeSet<>();
        List<Path> radici = new ArrayList<>();
        // la cartella in cui stanno le classi di test compilate
        radici.add(Path.of(ContrattoOstacoloTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
        for (String voce : System.getProperty("java.class.path").split(java.io.File.pathSeparator)) {
            radici.add(Path.of(voce));
        }
        for (Path radice : radici) {
            Path cartella = radice.resolve("ostacoli");
            if (!Files.isDirectory(cartella)) {
                continue;
            }
            try (Stream<Path> file = Files.list(cartella)) {
                for (Path p : (Iterable<Path>) file::iterator) {
                    String nomeFile = p.getFileName().toString();
                    if (!nomeFile.endsWith(".class") || nomeFile.contains("$")) {
                        continue;
                    }
                    String nome = "ostacoli." + nomeFile.substring(0, nomeFile.length() - ".class".length());
                    if (!visti.add(nome)) {
                        continue;
                    }
                    Class<?> c = carica(nome);
                    if (c != null && ContrattoOstacoloTest.class.isAssignableFrom(c)
                            && !Modifier.isAbstract(c.getModifiers())) {
                        trovate.add(c);
                    }
                }
            }
        }
        return trovate;
    }

    private static Class<?> carica(String nome) {
        try {
            return Class.forName(nome, false, OgniOstacoloHaIlTestDelContrattoTest.class.getClassLoader());
        } catch (ClassNotFoundException | LinkageError e) {
            return null;
        }
    }
}
