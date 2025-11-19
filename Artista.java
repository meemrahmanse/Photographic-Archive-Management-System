package progettoarchivio;

import java.util.Objects;

/**
 * Rappresenta un artista con attività prevalente.
 * Se l'attività non è presente nell'enum, viene salvata come testo personalizzato.
 */
public class Artista extends Personaggio {

    private final AttivitaPrevalente tipoAttivita;
    private final String attivitaCustom;        //null se non è altro
    private final String descrizioneAttivita;  //label standard/testo personalizzato

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
        super(key, nome, Genere.daChar(sesso), morte, nascita);

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

   // Costruisce un Artista con attività prevalente scelta direttamente dall'enum.
    
    public Artista(String key, String nome, char sesso, boolean morte, int nascita, AttivitaPrevalente tipo) {
        
        super(key, nome, Genere.daChar(sesso), morte, nascita);
        
        if (tipo == AttivitaPrevalente.ALTRO) {
            
            throw new IllegalArgumentException("Per attività personalizzata usa il costruttore con parametro String!");
        }
        
        this.tipoAttivita = tipo;
        this.attivitaCustom = null;
        this.descrizioneAttivita = tipo.getLabel();
    }

/**
     * Valida e normalizza l'attività testuale.
     *
     * @param attivita attività inserita dall'utente
     * @return versione trimmata
     * @throws IllegalArgumentException se nulla o vuota dopo trim
*/
    
    private static String validaAttivita(String attivita) {
        
        if (attivita == null) {
            
            throw new IllegalArgumentException("L'inserimento dell'attività prevalente è obbligatoria!");
        }
        String trimmed = attivita.trim();
        
        if (trimmed.isEmpty()) {
            
            throw new IllegalArgumentException("L'attività prevalente non può essere vuota!");
        }
        return trimmed;
    }

    public AttivitaPrevalente getTipoAttivita() {
        return tipoAttivita;
    }
/**
     * Restituisce l'attività personalizzata, se presente.
     * @return testo personalizzato o stringa vuota se non presente
*/
    
    public String getAttivitaCustom() {
        return attivitaCustom != null ? attivitaCustom : "";
    }

/**
     * Restituisce la descrizione completa dell'attività (label enum o testo custom).
     * @return descrizione leggibile
*/
    public String getDescrizioneAttivita() {
        return descrizioneAttivita;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " - Artista: " + descrizioneAttivita;
    }

    @Override
    public String toString() {
        return super.toString() + " - Artista: " + descrizioneAttivita;
    }
}
