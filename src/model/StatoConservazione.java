package model;

public enum StatoConservazione {

    BUONO,
    DANEGGIATO,
    PESSIMO,
    RESTAURATO;

        /**
     * Converte una stringa in StatoConservazione.
     * Accetta valori case-insensitive: "buono", "danneggiato", "pessimo", "restaurato".
     * @param s stringa da convertire
     * @return StatoConservazione corrispondente
     * @throws IllegalArgumentException se non valido
     */
    
    public static StatoConservazione fromString(String s) {
        if (s == null || s.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Inserire uno stato di conservazione!");
    }

    String upper = s.trim().toUpperCase();

    return switch (upper) {
        
        case "BUONO" -> BUONO;
        case "DANEGGIATO" -> DANEGGIATO;
        case "PESSIMO" -> PESSIMO;
        case "RESTAURATO" -> RESTAURATO;

        default -> {
                String validValues = String.join(", ", java.util.Arrays.stream(StatoConservazione.values()).map(Enum::name).map(String::toLowerCase).toArray(String[]::new));
                
                throw new IllegalArgumentException("Valore non valido: '" + s + "'. Valori ammessi: " + validValues + ".");
            }
        };
    }
}


