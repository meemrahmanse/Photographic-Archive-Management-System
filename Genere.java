package progettoarchivio;

/**
 * Enum per rappresentare il sesso di una persona.
 * M (Maschio), F (Femmina), A (Altro).
 */

public enum Genere {
    
    M("Maschio"),
    F("Femmina"),
    A("Altro");

    private final String descrizione;

    /** Costruttore privato per la descrizione leggibile. */

    Genere(String descrizione) {
        
        this.descrizione = descrizione;
    }
    public String getEtichetta() { 
        return descrizione; 
    }

    /** Restituisce la descrizione leggibile */
    
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }

/**
     * Converte un char in Genere.
     * @param c carattere ('M', 'm', 'F', f', 'A', 'a')
     * @return sesso corrispondente
     * @throws IllegalArgumentException se non valido
     */
    
    public static Genere daChar(char c) {
        
        return switch (Character.toUpperCase(c)) {
            
            case 'M' -> M;
            case 'F' -> F;
            case 'A' -> A;
            default -> throw new IllegalArgumentException(
                    
                "\nGenere non valido: '%c'. Perfavore inserisca 'M' per maschio, 'F' per femmina oppure 'A' per altro!".formatted(c)
            );
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
        
        if (s == null) {
            
            throw new IllegalArgumentException("\nQuesto campo è obbligatorio, perfavore inserisca il sesso!");
        }
    
    return switch (s.trim().toUpperCase()) {
        
        case "M", "MASCHIO" -> M;
        case "F", "FEMMINA" -> F;
        case "A", "ALTRO"-> A;
            
        default -> throw new IllegalArgumentException("Sesso non valido: '%s'.".formatted(s));
    };
}

}
