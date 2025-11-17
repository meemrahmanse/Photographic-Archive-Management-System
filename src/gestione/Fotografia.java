// Fotografia.java - meem

package gestione;

import java.io.Serializable;
import progettoarchivio.Soggetto;


// represent photograph with its basic attributes
public class Fotografia implements Serializable {

    private String idFoto;
    private String dimensione;
    private String statoConservazione;
    private Soggetto soggetto;

    // java constructor
    public Fotografia(String idFoto, String dimensione, String statoConservazione, Soggetto soggetto) {

        this.idFoto = idFoto;
        this.dimensione = dimensione;
        this.statoConservazione = statoConservazione;
        this.soggetto = soggetto;
    }



    public String getIdFoto() {
        return idFoto;
    }


    public void setIdFoto(String idFoto) {
        this.idFoto = idFoto;
    }


    public String getDimensione() {
        return dimensione;
    }


    public void setDimensione(String dimensione) {
        this.dimensione = dimensione;
    }


    public String getStatoConservazione() {
        return statoConservazione;
    }


    public void setStatoConservazione(String statoConservazione) {
        this.statoConservazione = statoConservazione;
    }


    public Soggetto getSoggetto() {
        return soggetto;
    }


    public void setSoggetto(Soggetto soggetto) {
        this.soggetto = soggetto;
    }

    
    // string representation of the photograph
    @Override
    public String toString() {
        return "ID: " + idFoto + ", Dimensione: " + dimensione + ", Stato: " + statoConservazione + ", Soggetto: " + soggetto.toString();
    }
}

// this class represents a photograph with its basic attributes.