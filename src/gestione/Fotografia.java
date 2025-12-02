// Fotografia.java - meem

package progettoarchivio;

import java.util.Objects;
        
/**
 * Rappresenta una fotografia con attributi di base:
 * - ID univoco
 * - Dimensione
 * - Stato di conservazione (enum)
 * - Soggetto fotografato
 */

public class Fotografia {

    private String idFoto;
    private int altezza;
    private int larghezza;
    private StatoConservazione statoConservazione;
    private Soggetto soggetto;

    // costruttore principale
    public Fotografia(String idFoto, int altezza, int larghezza, StatoConservazione statoConservazione, Soggetto soggetto) {

        if (idFoto == null || idFoto.trim().isEmpty()) {
            
            throw new IllegalArgumentException("ID foto non valido!");
        }
        
        if (altezza <= 0 || larghezza <= 0) {
            
            throw new IllegalArgumentException("La dimensione deve essere positiva!");
        }
        
        this.idFoto = idFoto.trim();
        this.altezza = altezza;
        this.larghezza = larghezza;
        this.statoConservazione = Objects.requireNonNull(statoConservazione, "Il stato di conservazione non puo essere vuoto!");
        this.soggetto = soggetto;
    }

    //costruttore vuoto
    public Fotografia() {}

    //getter & setter
    
    public String getIdFoto() {
        
        return idFoto;
    }


    public void setIdFoto(String idFoto) {
        
        if (idFoto == null || idFoto.trim().isEmpty()){
        
            throw new IllegalArgumentException("ID foto non valido!");
        }
        this.idFoto = idFoto.trim();
    }


    public int getAltezza() {
        
        return altezza;
    }

    public int getLarghezza(){
        
        return larghezza;
    }

    public void setAltezza(int altezza) {
        
        if (altezza <= 0) {
            
            throw new IllegalArgumentException("La dimensione deve essere positiva!");
        }
        this.altezza = altezza;
    }

    public void setLarghezza(int larghezza) {
        
        if (larghezza <= 0) {
            
            throw new IllegalArgumentException("La dimensione deve essere positiva!");
        }
        this.larghezza = larghezza;
    }
    public StatoConservazione getStatoConservazione() {
        
        return statoConservazione;
    }

    /**
     * Setter con conversione da String → enum.
     */
    
    public void setStatoConservazione(String statoConservazione) {
        
    if (statoConservazione == null || statoConservazione.isEmpty()) {
        
        throw new IllegalArgumentException("Stato di conservazione non valido!");
    }
    try {
        
        this.statoConservazione = StatoConservazione.valueOf(statoConservazione.trim(). toUpperCase());
    } catch (IllegalArgumentException e) {
        
        throw new IllegalArgumentException("Valore non riconosciuto per stato di conservazione: " + statoConservazione);
    }
}



    public Soggetto getSoggetto() {
        
        return this.soggetto;
    }


    public void setSoggetto(Soggetto soggetto) {
        this.soggetto = soggetto;
    }

    
    // string representation of the photograph
    @Override
    
    public String toString() {
        return "Fotografia [ID: " + idFoto + ", Dimensione: " + altezza + " x " + larghezza + ", Stato: " + statoConservazione + ", Soggetto: " + soggetto + "]";
    }
    
    @Override
    
    public boolean equals(Object obj) {
        
        if (this == obj) {
            
            return true;
        }
        
        if (!(obj instanceof Fotografia)) {
            
            return false;
        }
        Fotografia other = (Fotografia) obj;
        
        return idFoto != null && idFoto.equals(other.idFoto);
    }

    @Override
    public int hashCode() {
        
        return idFoto != null ? idFoto.hashCode() : 0;
    }
}


// this class represents a photograph with its basic attributes.
