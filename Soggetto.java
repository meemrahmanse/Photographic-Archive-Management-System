//Classe astratta per rappresentare un soggetto fotografato.
//Ogni soggetto ha una chiave univoca per il catalogo

package progettoarchivio;

import java.util.Objects;


public abstract class Soggetto {
    
    private final String key; //final = immodificabile
    
/**
     * Costruisce un Soggetto con chiave valida.
     * @param key chiave del soggetto (non null, non vuota, solo A-Z e 0-9)
     * @throws IllegalArgumentException se la chiave è invalida
*/

    public Soggetto(String key) {
        
        if (key == null || key.trim().isEmpty()) {   //trim = tolgo spazi all'inizio e fine
            
            throw new IllegalArgumentException ("\nQuesto campo non puo essere vuoto, perfavore inserisca la chiave (puo contere solo numeri e lettere!)");
        }
        
        String normalized = key.trim().toUpperCase();
        
        if (!normalized.matches("[A-Z0-9]+")) {  //matches = controlla che siano solo lettere e numeri, se no ! e lancia la eccezione
            
            throw new IllegalArgumentException("\nLa chiave può contenere solo lettere e numeri, perfavore inserisca la chiave giusta!");
        }

        this.key = normalized;
    }

    public String getKey() {
        return key;
    }
    
/** Restituisce una descrizione testuale del soggetto */
    public abstract String getDescription();
    
    /**
     * Verifica se la chiave contiene la query 
     * Query = stringa di ricerca inserita dall’utente per trovare un soggetto nella chiave.
     * @param query stringa da cercare (può essere null)
     * @return true se la chiave contiene la query
     */
    
    public boolean matchesKey(String query) {
        
    if (query == null) {    //se il campo è vuoto non posso ricercarlo
        
        return false;
    }

    //true se parte è dentro key, false altrimenti es: chiave=ABC123, query=123 → true
    return key.contains(query.trim().toUpperCase());
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
        
        return Objects.hash(key);    //hash per confrontare più velocemente gli oggetti
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
