package progettoarchivio;

import java.util.Objects;

/**
 * Rappresenta un artista con attività prevalente.
 * Se l'attività non è presente nell'enum, viene salvata come testo personalizzato.
 */
public class Artista extends Personaggio {

    private final AttivitaPrevalente tipoAttivita;
    private final String attivitaCustom;        
    private final String descrizioneAttivita;  

    /**
     * Costruisce Personaggio
     * @param key = chiave univoca
     * @param nome = nome completo
     * @param sesso = 'M', 'F' o 'A'
     * @param morte = true se deceduto
     * @param nascita = anno di nascita
     * @param attivita = attività prevalente testuale 
     * @throws IllegalArgumentException se l'attività vuota
     */
    public Artista(String key, String nome, char sesso, boolean morte, int nascita, String attivita) {
        super(key, nome, sesso, morte, nascita);

        String attivitaTrimmed = validaAttivita(attivita);

        // prova a mappare sull'enum
        AttivitaPrevalente tipo = AttivitaPrevalente.daStringa(attivitaTrimmed);

        if (tipo == AttivitaPrevalente.ALTRO) {
            
            // Non trovato nell'enum → uso il testo originale come custom
            this.tipoAttivita = AttivitaPrevalente.ALTRO;
            this.attivitaCustom = attivitaTrimmed;
            this.descrizioneAttivita = attivitaTrimmed;  
        } 
        else {
            // Trovato nell'enum → uso la label standard
            this.tipoAttivita = tipo;
            this.attivitaCustom = null;
            this.descrizioneAttivita = tipo.getLabel();  
        }
    }

    // Costruttore alternativo: accetta direttamente l'enum 
    public Artista(String key, String nome, char sesso, boolean morte, int nascita, AttivitaPrevalente tipo) {
        
        super(key, nome, sesso, morte, nascita);
        
        if (tipo == AttivitaPrevalente.ALTRO) {
            
            throw new IllegalArgumentException("Usa il costruttore con String per attività personalizzata");
        }
        this.tipoAttivita = tipo;
        this.attivitaCustom = null;
        this.descrizioneAttivita = tipo.getLabel();
    }

    private static String validaAttivita(String attivita) {
        
        if (attivita == null || attivita.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Per favore inserisca l'attivita prevalente!");
        }
        return attivita.trim();
    }

    public AttivitaPrevalente getTipoAttivita() {
        return tipoAttivita;
    }

    public String getAttivitaCustom() {
        return attivitaCustom;
    }

    public String getDescrizioneAttivita() {
        return descrizioneAttivita;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " - Artista: " + descrizioneAttivita;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + getDescription();
    }
}
