package progettoarchivio;

import java.util.Map;
import java.util.HashMap;

/**
 * Enum per le attività prevalenti di un artista.
 * Se non trovato → ALTRO
 */

public enum AttivitaPrevalente {
    
    PITTURA("Pittura", "pittura", "pittore", "pittrice"),
    SCULTURA("Scultura", "scultura", "scultore", "scultrice"),
    FOTOGRAFIA("Fotografia", "fotografia", "fotografo", "fotografa"),
    ARCHITETTURA("Architettura", "architettura", "architetto", "architetta"),
    GRAFICA("Grafica", "grafica", "grafico", "grafica"),
    DESIGN("Design", "design", "designer"),
    ALTRO("Attività personalizzata");
    
    private final String label; //es pittura
    private final String[] aliases; //es pittrice

/** Mappa per ricerca rapida di alias e nomi chiave maiuscola → valore enum */
    private static final Map<String, AttivitaPrevalente> LOOKUP = new HashMap<>();

    //mappa inizializzata 1 volta
    
    static {
        
        for (AttivitaPrevalente attivita : values()) { //scorrimento tutti valori
            
            LOOKUP.put(attivita.name().toUpperCase(), attivita);  //nome enum
            
            for (String a : attivita.aliases) {    //aggiunta allias 
                
                LOOKUP.put(a.toUpperCase(), attivita);    //put = aggiunge la chiave, se ce gia la aggiorna
            }
        }
    }

/**
     * Costruttore con descrizione e alias.
     * @param descrizione nome formale (es. "Pittura")
     * @param alias vari termini associati (es. "pittore")
     *String... = puo contenere sequenza caratteri
*/
    
    AttivitaPrevalente(String label, String... aliases) {
        
        this.label = label;
        this.aliases = aliases;
    }

/** Restituisce il nome formale dell'attività */
    
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

/**
     * Converte stringa in enum
     * Accetta: nome enum, alias, o altri varianti case-insensitive.
     * @param input stringa da convertire 
     * @return valore corrispondente, o ALTRO se non trovato
     * @throws IllegalArgumentException se input è null o vuoto
*/
    
    public static AttivitaPrevalente daStringa(String input) {
        
        if (input == null || input.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Questo campo è obbligatorio, perfavore inserisca l'attività prevalente!");
        }
        String chiave = input.trim().toUpperCase();
        
        return LOOKUP.getOrDefault(chiave, ALTRO);
        //getOrDefault cerca nella mappa se trova -> valore se no -> altro
    }

}

