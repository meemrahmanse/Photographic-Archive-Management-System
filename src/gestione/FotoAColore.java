// FotoAColore.java - meem

package gestione;

import progettoarchivio.Soggetto;

public class FotoAColore extends Fotografia {

    private String tipoStampa; // "chiaro" o "opaco"

    public FotoAColore(String idFoto, String dimensione, String statoConservazione, Soggetto soggetto, String tipoStampa) {

        super(idFoto, dimensione, statoConservazione, soggetto);
        this.tipoStampa = tipoStampa;

    }

    public FotoAColore() {}


    // Getter e Setter
    public String getTipoStampa() {
        return tipoStampa;
    }


    public void setTipoStampa(String tipoStampa) {
        this.tipoStampa = tipoStampa;
    }

    
    @Override
    public String toString() {
        return super.toString() + ", Tipo Stampa: " + tipoStampa;
    }
}


// this class represents a color photograph with a specific type of print (glossy or matte).
