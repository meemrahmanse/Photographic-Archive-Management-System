package progettoarchivio;

public enum StatoConservazione {

    OTTIMO,
    BUONO,
    DISCRETO,
    SCARSO;

    /**
     * Accetta: "OTTIMO", "BUONO", "DISCRETO", "SCARSO"
     * @param s stringa da convertire
     * @return valore enum corrispondente
     * @throws IllegalArgumentException se non valido
     */
    public static StatoConservazione fromString(String s) {

        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Inserire uno stato di conservazione valido!");
        }

        return switch (s.trim().toUpperCase()) {
            case "OTTIMO"   -> OTTIMO;
            case "BUONO"    -> BUONO;
            case "DISCRETO" -> DISCRETO;
            case "SCARSO"   -> SCARSO;

            default -> throw new IllegalArgumentException("Stato di conservazione non valido: '%s'. Valori ammessi: BUONO, DANEGGIATO, PESSIMO, RESTAURATO.".formatted(s));
        };
    }

    /**
     * Override per una rappresentazione leggibile.
     * charAt0 prende la prima lettera al maiuscolo e tutte le altre invece al minuscolo
     */
    @Override
    
    public String toString() {
        
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
