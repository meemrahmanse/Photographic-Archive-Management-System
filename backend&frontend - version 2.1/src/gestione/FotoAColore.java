// FotoAColore.java - meem
package gestione;

import progettoarchivio.Soggetto;  

public class FotoAColore extends Fotografia {

    private String tipoStampa; // tindicates the type of print: glossy or matte

    public FotoAColore(String idFoto, String dimensione, String statoConservazione, Soggetto soggetto, String tipoStampa) {

        super(idFoto, dimensione, statoConservazione, soggetto);
        this.tipoStampa = tipoStampa;

    }



    // Getter e Setter
    public String getTipoStampa() {
        return tipoStampa;
    }

    // Setter for tipoStampa
    public void setTipoStampa(String tipoStampa) {
        this.tipoStampa = tipoStampa;
    }

    
    @Override // overrides the toString method to include the type of print
    public String toString() {
        return super.toString() + ", Tipo Stampa: " + tipoStampa;
    }
}

// this class represents a color photograph with a specific type of print (glossy or matte).