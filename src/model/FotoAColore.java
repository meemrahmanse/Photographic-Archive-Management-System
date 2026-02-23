// FotoAColore.java - meem

package model;

import java.time.LocalDate;

/**
 * Foto a colori: estende Fotografia aggiungendo il tipo di stampa
 * (es. "chiaro", "opaco").
 */

public class FotoAColore extends Fotografia {

    private String tipoStampa; // "chiaro" o "opaco"
    
  // costruttore principale che delega al costruttore di Fotografia
    
    public FotoAColore(String idFoto, int altezza, int larghezza, StatoConservazione stato, Soggetto soggetto, String tipoStampa, String titolo, String autore, LocalDate data) {

        super(idFoto, altezza, larghezza, stato, soggetto, titolo, autore, data);
        setTipoStampa(tipoStampa);

    }

//costruttore vuoto
    public FotoAColore() {
    
    super();
    this.tipoStampa = "chiaro";
    }


    // Getter e Setter
    public String getTipoStampa() {
        
        return this.tipoStampa;
    }


    public void setTipoStampa(String tipoStampa) {
        
          if (tipoStampa == null || tipoStampa.trim().isEmpty()) {
            throw new IllegalArgumentException("Il tipo di stampa non può essere vuoto!");
        }

        String valore = tipoStampa.trim().toLowerCase();

        if (!valore.equals("chiaro") && !valore.equals("opaco")) {
            
            throw new IllegalArgumentException("Tipo di stampa non valido: '" + tipoStampa +"'. Valori ammessi: chiaro, opaco");
        }

        this.tipoStampa = valore;
    }

    
    @Override
    
    public String toString() {
        
        return super.toString() + ", Tipo di stampa: " + tipoStampa;
    }
}


// this class represents a color photograph with a specific type of print (glossy or matte).
