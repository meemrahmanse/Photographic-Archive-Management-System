package progettoarchivio;

/**
 * Enum per rappresentare il sesso di una persona.
 * M (Maschio), F (Femmina), A (Altro).
 */

public enum Genere {
    
    M("Maschio"),
    F("Femmina"),
    A("Altro");

    private final String etichetta;

    /** Costruttore privato per la etichetta leggibile. */

    Genere(String etichetta) {
        
        this.etichetta = etichetta;
    }

/** Restituisce l'etichetta leggibile del genere. */
    
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
            default -> throw new IllegalArgumentException("Sesso non valido: '%c'. Per favore usi 'M', 'F' oppure 'A'!".formatted(c));
        };
    }
    
    /**
     * Converte una stringa in Genere 
     * Accetta: "M", "MASCHIO", "F", "FEMMINA", "A", "ALTRO"
     * @param s = stringa da convertire 
     * @return Genere corrispondente
     * @throws IllegalArgumentException se stringa non valida
     */
    
    public static Genere fromString(String s) {
        
        if (s == null || s.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Per favore inserisca il sesso!");
        }
    
        
    return switch (s.trim().toUpperCase()) {
        
        case "M", "MASCHIO" -> M;
        case "F", "FEMMINA" -> F;
        case "A", "ALTRO", "X" -> A;
            
        default -> throw new IllegalArgumentException(String.format("Sesso non valido: '%s'. Usa 'M', 'F' oppure 'A'.", s));
    };
}
/**
     * Override di toString() per una rappresentazione più leggibile.
*/
    
    @Override
    public String toString() {
        
        return etichetta;
    }
}
