package progettoarchivio;

/**
 * Rappresenta un artista con attività prevalente.
 * Se l'attività non è nell'enum, usa testo personalizzato.
 */

public class Artista extends Personaggio {
    
    private final AttivitaPrevalente tipoAttivita;
    private final String attivitaCustom;
    private final String descrizioneAttivita;
    
    /**
     * Costruisce un Artista.
     * @param key chiave univoca (validata in Soggetto)
     * @param nome = nome
     * @param sesso = 'M', 'F', 'A'
     * @param morte true se deceduto
     * @param nascita anno di nascita
     * @param attivita = attività prevalente (obbligatoria, trimmed)
     */

    public Artista(String key, String nome, char sesso, boolean morte, int nascita, String attivita) {
        
        super(key, nome, sesso, morte, nascita);
        
        String attivitaValidata = validateActivity(attivita);
        
        //daStringa = converte la stringa in enum; vedo se sta nell'enum
        this.tipoAttivita = AttivitaPrevalente.daStringa(attivitaValidata);
        
        if (this.tipoAttivita == AttivitaPrevalente.ALTRO) {
            //se non è nell'enum salvo come testo
            
            this.attivitaCustom = (tipoAttivita == AttivitaPrevalente.ALTRO) ? attivitaValidata : null;
            
            this.descrizioneAttivita = (attivitaCustom != null) ? attivitaCustom : tipoAttivita.getLabel();

        } 
            
        else {
            
            this.attivitaCustom = null;
            this.descrizioneAttivita = this.tipoAttivita.getLabel(); // Usa descrizione enum
        }
    }

    private String validateActivity(String activity) {
        
        if (activity == null || activity.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Questo campo non puo essere vuoto, perfavore inserisca l'attività prevalente!");
        }
        return activity.trim();
    }

    public String getDescrizioneAttivita() { 
        
        return descrizioneAttivita; 
    }
    
    public AttivitaPrevalente getTipoAttivita() {
        return tipoAttivita;
    }

    public String getAttivitaCustom() {
        return attivitaCustom;
    }

    @Override
    public String getDescription() {
        
        return super.getDescription() + " - Artista: " + descrizioneAttivita;
    }

    
    @Override
    
    public String toString() {
        
    return String.format("%s - %s", super.toString(), getDescription());
    
    }
}

