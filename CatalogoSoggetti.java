package progettoarchivio;

import java.util.*;

/**
 * Catalogo centrale dei soggetti fotografati.
 * Implementa il pattern Singleton = modello generativo che garantisce l'esistenza di un solo oggetto di una classe e consente di accedere all'oggetto da qualsiasi luogo
 * Garantisce unicità delle chiavi e accesso globale.
 */

public final class CatalogoSoggetti {

    /** Unica istanza del catalogo */
    
    private static final class Holder {
        
        static final CatalogoSoggetti ISTANZA = new CatalogoSoggetti();
    }

    /** Mappa: chiave (maiuscola, trimmed) → Soggetto */
    
    private final Map<String, Soggetto> soggetti;

    /** Costruttore privato = inizializza la mappa */
    
    private CatalogoSoggetti() {
        
        this.soggetti = new HashMap<>();
    }

    /** Restituisce l'unica istanza del catalogo */
    
    public static CatalogoSoggetti getInstance() {
        
        return Holder.ISTANZA;        //contenitore generico o classe wrapper in grado di memorizzare e gestire un oggetto di qualsiasi tipo
    }

    /**
     * Aggiunge un soggetto al catalogo.
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
    }

    /**
     * Cerca un soggetto per chiave 
     * @param chiave = chiave da cercare 
     * @return Soggetto corrispondente
     * @throws IllegalArgumentException se chiave invalida
     * @throws NoSuchElementException = non trovato
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
     * @param chiave = chiave del soggetto da rimuovere
     * @return true se rimosso, false se non esiste
     */
    
    public boolean rimuoviSoggetto(String chiave) {
        
        if (chiave == null || chiave.trim().isEmpty()) return false;
        
        return soggetti.remove(normalizzaChiave(chiave)) != null;
    }

    /**
     * Restituisce una lista non modificabile di tutti i soggetti.
     * @return collezione = di sola lettura
     * Collections = esclusivamente da metodi statici che operano su collezioni o le restituiscono
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
