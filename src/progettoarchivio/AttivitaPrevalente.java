package progettoarchivio;

public enum AttivitaPrevalente {
    PITTURA("Pittura"),
    SCULTURA("Scultura"),
    MUSICA("Musica"),
    ALTRO("Altro");

    private final String label;

    AttivitaPrevalente(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AttivitaPrevalente daStringa(String s) {
        
        if (s == null) {
            
            return ALTRO;
        }
        String str = s.trim();
        
        for (AttivitaPrevalente a : values()) {
            
            if (a.label.equalsIgnoreCase(str)){
                
                return a;
            }
        }
        return ALTRO;
    }
}
