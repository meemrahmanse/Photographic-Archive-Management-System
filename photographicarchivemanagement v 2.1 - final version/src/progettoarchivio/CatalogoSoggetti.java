package progettoarchivio;

import database.JsonDatabase;
import java.util.*;

/**
 * Catalogo centrale dei soggetti fotografati.
 * Implementa il pattern Singleton = modello generativo che garantisce
 * l'esistenza di un solo oggetto di una classe e consente di accedere
 * all'oggetto da qualsiasi luogo
 * Garantisce unicità delle chiavi e accesso globale.
 * Ora supporta JSON database per persistenza.
 */

public final class CatalogoSoggetti {

    /** Unica istanza del catalogo */

    private static final class Holder {

        static final CatalogoSoggetti ISTANZA = new CatalogoSoggetti();
    }

    /** Mappa: chiave (maiuscola, trimmed) → Soggetto */

    private final Map<String, Soggetto> soggetti;

    /** Costruttore privato = inizializza la mappa e carica da JSON */

    private CatalogoSoggetti() {
        this.soggetti = new HashMap<>();
        caricaDaFile();

        // Automatic initialization if empty
        if (soggetti.isEmpty()) {
            initDefaultSamples();
        }
    }

    private void initDefaultSamples() {
        try {
            aggiungiSoggetto(new Personaggio("p1", "Mario Rossi", 'M', false, 1980));
            aggiungiSoggetto(new Politico("p2", "Luigi Verdi", 'M', false, 1970, "Centro", "Ministro"));
            aggiungiSoggetto(new Artista("p3", "Anna Bianchi", 'F', true, 1950, "Pittura"));
            aggiungiSoggetto(new Luogo("l1", "Colosseo", "Anfiteatro romano a Roma"));
            aggiungiSoggetto(new Oggetto("o1", "Vaso Ming", "Antico vaso cinese"));
            salvaSuFile();
        } catch (Exception e) {
            System.err.println("Failed to init default samples: " + e.getMessage());
        }
    }

    /** Restituisce l'unica istanza del catalogo */

    public static CatalogoSoggetti getInstance() {

        return Holder.ISTANZA; // contenitore generico o classe wrapper in grado di memorizzare e gestire un
                               // oggetto di qualsiasi tipo
    }

    /**
     * Salva tutti i soggetti nel file JSON
     */
    public void salvaSuFile() {
        JsonDatabase.salvaSoggetti(soggetti.values());
    }

    /**
     * Carica i soggetti dal file JSON
     */
    public void caricaDaFile() {
        List<Soggetto> loaded = JsonDatabase.caricaSoggetti();
        soggetti.clear();
        for (Soggetto s : loaded) {
            soggetti.put(normalizzaChiave(s.getKey()), s);
        }
    }

    /**
     * Aggiunge un soggetto al catalogo.
     * 
     * @param s = soggetto da aggiungere
     * @throws IllegalArgumentException se s è null o chiave già esistente
     */

    public void aggiungiSoggetto(Soggetto s) {

        if (s == null) {

            throw new IllegalArgumentException("\nQuesto campo è obbligatorio, perfavore inserisca il soggetto!");
        }
        String chiave = normalizzaChiave(s.getKey());

        if (soggetti.containsKey(chiave)) {

            throw new IllegalArgumentException("\nChiave già esistente: '" + chiave + "'");
        }
        soggetti.put(chiave, s);
        salvaSuFile(); // Automatic save
    }

    /**
     * Cerca un soggetto per chiave
     * 
     * @param chiave = chiave da cercare
     * @return Soggetto corrispondente
     * @throws IllegalArgumentException se chiave invalida
     * @throws NoSuchElementException   = non trovato
     */

    public Soggetto trovaPerChiave(String chiave) {

        String k = normalizzaChiave(chiave);
        Soggetto s = soggetti.get(k);

        if (s == null) {

            throw new NoSuchElementException("Soggetto non trovato con la chiave: '" + k + "'");
        }
        return s;
    }

    /**
     * Rimuove un soggetto dal catalogo.
     * 
     * @param chiave = chiave del soggetto da rimuovere
     * @return true se rimosso, false se non esiste
     */

    public boolean rimuoviSoggetto(String chiave) {

        if (chiave == null || chiave.trim().isEmpty())
            return false;

        boolean removed = soggetti.remove(normalizzaChiave(chiave)) != null;
        if (removed) {
            salvaSuFile(); // Automatic save
        }
        return removed;
    }

    /**
     * Restituisce una lista non modificabile di tutti i soggetti.
     * 
     * @return collezione = di sola lettura
     *         Collections = esclusivamente da metodi statici che operano su
     *         collezioni o le restituiscono
     */

    public Collection<Soggetto> tuttiSoggetti() {

        return Collections.unmodifiableCollection(soggetti.values());
    }

    /** Restituisce il numero di soggetti nel catalogo */

    public int dimensione() {

        return soggetti.size();
    }

    /**
     * Chiave con trim + maiuscolo.
     * Usata per uniformità.
     */

    private String normalizzaChiave(String chiave) {

        if (chiave == null || chiave.trim().isEmpty()) {

            throw new IllegalArgumentException("Questo campo è obbligatorio, perfavore inserisca la chiave!");
        }
        return chiave.trim().toUpperCase();
    }

}
