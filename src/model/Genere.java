package model;

/**
 * Enum che rappresenta il genere di una persona.
 * I valori possibili sono M (Maschio), F (Femmina) e A (Altro).
 */

public enum Genere {
    
    M("Maschio"),
    F("Femmina"),
    A("Altro");

    private final String etichetta;

    /**
     * Costruisce il valore dell'enum con la sua etichetta leggibile.
     * @param etichetta etichetta testuale da mostrare all'utente (es. "Maschio")
     */

    Genere(String etichetta) {
        
        this.etichetta = etichetta;
    }

    /**
     * Restituisce l'etichetta leggibile del genere.
     * @return etichetta testuale (es. "Maschio", "Femmina", "Altro")
     */
    
    public String getEtichetta() { 
        
        return etichetta; 
    }


/**
     * Converte un char in Genere.
     * Accetta "M", "F", "A" maiuscolo o minuscolo 
     * @param c carattere
     * @return sesso corrispondente
     * @throws IllegalArgumentException se non valido
     */
    
    public static Genere daChar(char c) {
        
        return switch (Character.toUpperCase(c)) {
            
            case 'M' -> M;
            case 'F' -> F;
            case 'A' -> A;
            default -> throw new IllegalArgumentException("Errore: Genere non valido: '%c'. Per favore, inserisca 'M', 'F' oppure 'A'!".formatted(c));
        };
    }
    
    /**
     * Converte una stringa in Genere 
     * Accetta: "M", "MASCHIO", "F", "FEMMINA", "A", "ALTRO"
     * @param s stringa da convertire 
     * @return Genere corrispondente
     * @throws IllegalArgumentException se stringa non valida
     */
    
    public static Genere fromString(String s) {
        
        if (s == null || s.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Errore: Questo campo non può essere vuoto!");
        }
        String upper = s.trim().toUpperCase();
        
    return switch (upper) {
        
        case "M", "MASCHIO" -> M;
        case "F", "FEMMINA" -> F;
        case "A", "ALTRO", "X" -> A;
            
        default -> throw new IllegalArgumentException(String.format("Errore: genere non valido: '%s'. Utilizza 'M', 'F' oppure 'A'.", s));
    };
}

    /**
     * Restituisce l'etichetta leggibile al posto del nome della costante.
     * @return etichetta testuale (es. "Maschio" invece di "M")
     */
    
    @Override
    public String toString() {
        
        return etichetta;
    }
}

