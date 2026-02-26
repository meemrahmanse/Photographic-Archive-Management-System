package model;

/**
 * Enum che rappresenta le attività prevalenti di un artista.
 * Se l'attività non corrisponde a nessun valore standard, viene usato ALTRO.
 */

public enum AttivitaPrevalente {
	
    PITTURA("Pittura"),
    SCULTURA("Scultura"),
    MUSICA("Musica"),
    ALTRO("Altro");

    private final String label;
    
    /**
     * Costruisce il valore dell'enum con la sua etichetta leggibile.
     * @param label etichetta testuale da mostrare all'utente
     */

    AttivitaPrevalente(String label) {
    	
        this.label = label;
    }
    
    /**
     * Restituisce l'etichetta leggibile dell'attività.
     * @return etichetta testuale (es. "Pittura")
     */
    
    public String getLabel() {
    	
        return label;
    }
    
    /**
     * Converte una stringa in AttivitaPrevalente ignorando maiuscole/minuscole.
     * Se la stringa è nulla o non corrisponde a nessun valore, restituisce ALTRO.
     * @param s stringa da convertire
     * @return il valore corrispondente, oppure ALTRO se non trovato
     */

    public static AttivitaPrevalente daStringa(String s) {
        
        if (s == null) {
            
            return ALTRO;
        }
        
        String str = s.trim().toLowerCase();
        
        for (AttivitaPrevalente a : values()) {
            
            if (a.label.equalsIgnoreCase(str)){
                
                return a;
            }
        }
        return ALTRO;
    }
    
    /**
     * Restituisce l'etichetta leggibile al posto del nome costante.
     * @return etichetta testuale (es. "Pittura" invece di "PITTURA")
     */
    
    @Override
    
    public String toString() {
        
    	return label;
    
    }
}

