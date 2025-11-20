/**
    *Classe astratta per rappresentare un soggetto fotografato.
    *Ogni soggetto ha una chiave univoca per il catalogo
    * La chiave è immutabile, normalizzata in maiuscolo e validata al momento della costruzione.
*/
package progettoarchivio;

import java.util.Locale;
import java.util.Objects;

public abstract class Soggetto {
    
    private static final int MAX_KEY_LENGTH = 30;
    private final String key;     //final = immodificabile
    
/**
     * Costruisce un Soggetto con chiave valida.
     * @param key chiave del soggetto (obbligatoria e solo A-Z e 0-9)
     * @throws IllegalArgumentException se la chiave è invalida
*/

    public Soggetto(String key) {
        
        if (key == null || key.trim().isEmpty()) {   //trim = toglie spazi all'inizio e fine
            
            throw new IllegalArgumentException ("Chiave non valida, non può essere vuota!");
        }
        
        String normalized = key.trim().toUpperCase(Locale.ROOT);
        
        if (!normalized.matches("[A-Z0-9]+")) {  //matches = controlla che siano solo lettere e numeri, se no ! e lancia la eccezione
            
            throw new IllegalArgumentException("Chiave non valida, può contenere solo caratteri alfanumerici!");
        }

        if (normalized.length() > MAX_KEY_LENGTH) {
            
            throw new IllegalArgumentException("Chiave non valida, deve avere al massimo " + MAX_KEY_LENGTH + " caratteri!");
        }
        
        this.key = normalized;
    }

public Soggetto (){}
    
    public String getKey() {
        return key;
    }
    
/** 
*Restituisce una descrizione testuale del soggetto 
* @return descrizione leggibile del soggetto (es. nome, luogo, evento)
*/
    
    public abstract String getDescription();
    
    /**
     * Verifica se la chiave contiene la query 
     * Query = stringa di ricerca inserita dall’utente per trovare un soggetto nella chiave.
     * @param query stringa da cercare (può essere null)
     * @return true se la chiave contiene la query
     */
    
    public boolean matchesKey(String query) {
        
    if (query == null || query.trim().isBlank()) {    //se il campo è vuoto non posso ricercarlo
        return false;
    }

    //true se parte è dentro key, false altrimenti es: chiave=ABC123, query=123 → true    
    String normalizedQuery = query.trim().toUpperCase(Locale.ROOT);
    return key.contains(normalizedQuery);
  }


    @Override

    //true se due oggetti sono lo stesso soggetto (stessa classe + stessa chiave) -> evito confusione
    public boolean equals(Object o) {
        
        if (this == o){
            
            return true;
        }

        //Controlla se un oggetto appartiene a una certa classe
        
        if (!(o instanceof Soggetto s)) {  
            
            return false;
        }
        
        return getClass() == s.getClass() && key.equals(s.key);
    }

    @Override
    public int hashCode() {     //restituisce un numero intero basato sulla stringa
        
        return Objects.hash(getClass(), key);    //hash per confrontare più velocemente gli oggetti
    }

    @Override
    public String toString() {

        //String.format(...) → crea una stringa formattata con valori inseriti
        //s% = stampa nome classe e chiave ordinati in modo leggibile
        
        return String.format("%s[chiave=%s]", getClass().getSimpleName(), key);
    }
}
/** 
 * getClass() prende la classe reale, getSimpleName() solo il nome per avere solo il nome della classe, senza il pacchetto
 */
