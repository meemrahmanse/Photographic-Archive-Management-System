package model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * Catalogo centrale dei soggetti fotografati.
 * Implementa il pattern Singleton per garantire un'unica istanza condivisa.
 * Garantisce unicità delle chiavi e accesso globale.
 */

public final class CatalogoSoggetti {
	
    /** Unica istanza del catalogo */    
    private static final class Holder {
        
        static final CatalogoSoggetti ISTANZA = new CatalogoSoggetti();
    }

private CatalogoSoggetti (){}

    /** Mappa: chiave (maiuscola, trimmed) → Soggetto */  
    private final Map<String, Soggetto> soggetti = new ConcurrentHashMap<>(); 

    

    /** Restituisce l'unica istanza del catalogo */
    
    public static CatalogoSoggetti getInstance() {
        
        return Holder.ISTANZA;        
    }

    /**
     * Aggiunge un soggetto al catalogo.
     * @param s = soggetto da aggiungere 
     * @throws IllegalArgumentException se s è null o chiave già esistente
     */
    
    public void aggiungiSoggetto(Soggetto s) {
        
        if (s == null) {
            
            throw new IllegalArgumentException("\nErrore: Questo campo è obbligatorio, perfavore inserisca il soggetto!");
        }
        String chiave = normalizzaChiave(s.getKey());
        
        if (soggetti.containsKey(chiave)) {
            
            throw new IllegalArgumentException("\nErrore: Chiave già esistente: '" + chiave + "'");
        }
        soggetti.put(chiave, s);
    }

    /**
     * Cerca un soggetto per chiave 
     * @return Soggetto corrispondente
     * @throws IllegalArgumentException se chiave invalida
     * @param chiave chiave del soggetto da cercare
	 * @throws NoSuchElementException se il soggetto non viene trovato
     */
    
    public Soggetto trovaPerChiave(String chiave) {
        
        String k = normalizzaChiave(chiave);
        Soggetto s = soggetti.get(k);
        
        if (s == null) {
            
            throw new NoSuchElementException("Errore: il soggetto con la chiave: '" + k + "' non è stato trovato!");
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
     * Restituisce una collezione non modificabile di tutti i soggetti presenti nel catalogo.
     * @return collezione di sola lettura dei soggetti
     */
    
    public Collection<Soggetto> tuttiSoggetti() {
        
        return Collections.unmodifiableCollection(soggetti.values());
    }

    /** Restituisce il numero di soggetti nel catalogo */
    
    public int dimensione() {
        
        return soggetti.size();
    }

    /**
     * Normalizza la chiave rimuovendo spazi e convertendo in maiuscolo.
     * @param chiave la chiave da normalizzare
     * @return la chiave normalizzata
     * @throws IllegalArgumentException se la chiave è nulla o vuota
     */
    
    private String normalizzaChiave(String chiave) {
        
        if (chiave == null || chiave.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Errore: Questo campo è obbligatorio, perfavore inserisca la chiave!");
        }
        return chiave.trim().replaceAll("\\s+", "").toUpperCase();
    }

}

