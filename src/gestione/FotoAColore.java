// FotoAColore.java - meem

package progettoarchivio;

/**
 * Foto a colori: estende Fotografia aggiungendo il tipo di stampa
 * (es. "chiaro", "opaco").
 */

public class FotoAColore extends Fotografia {

    private String tipoStampa; // "chiaro" o "opaco"
    
  // costruttore principale che delega al costruttore di Fotografia
    
    public FotoAColore(String idFoto, String dimensione, String statoConservazione, Soggetto soggetto, String tipoStampa) {

        super(idFoto, Integer.parseInt(dimensione), StatoConservazione.valueOf(statoConservazione.toUpperCase()), soggetto);
        this.tipoStampa = tipoStampa != null ? tipoStampa.trim() : "";

    }

//costruttore vuoto
    public FotoAColore() {
    
    super();
    this.tipoStampa = "";
    }


    // Getter e Setter
    public String getTipoStampa() {
        
        return this.tipoStampa;
    }


    public void setTipoStampa(String tipoStampa) {
        
        this.tipoStampa = tipoStampa != null ? tipoStampa.trim() : "";
    }

    
    @Override
    
    public String toString() {
        
        return super.toString() + ", Tipo di stampa: " + tipoStampa;
    }
}


// this class represents a color photograph with a specific type of print (glossy or matte).
