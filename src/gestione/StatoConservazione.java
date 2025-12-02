package progettoarchivio;

public enum StatoConservazione {

    BUONO,
    DANEGGIATO,
    PESSIMO,
    RESTAURATO;

    /**
     * Accetta: "OTTIMO", "BUONO", "DISCRETO", "SCARSO"
     * @param s stringa da convertire
     * @return valore enum corrispondente
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

        default -> throw new IllegalArgumentException("Valore non valido: '" + s + "'. I valori ammessi sono: buono, daneggiato, pessimo, restaurato."
        );
    };
}
}
