package model;

import java.time.LocalDate;

/**
 * Rappresenta una fotografia a colori, estende Fotografia aggiungendo
 * il tipo di stampa. I valori ammessi sono "chiaro" e "opaco".
 */

public class FotoAColore extends Fotografia {

    private String tipoStampa; // valori ammessi "chiaro" o "opaco"
    
  // costruttore principale che delega al costruttore di Fotografia
    
    public FotoAColore(String idFoto, int altezza, int larghezza, StatoConservazione stato, Soggetto soggetto, String tipoStampa, String titolo, String autore, LocalDate data) {

        super(idFoto, altezza, larghezza, stato, soggetto, titolo, autore, data);
        setTipoStampa(tipoStampa);

    }

    /**
     * Costruttore vuoto necessario per la deserializzazione da file JSON.
     * Il tipo di stampa viene inizializzato a "chiaro" come valore di default.
     */
    
    public FotoAColore() {
    
    super();
    this.tipoStampa = "chiaro";
    }

    /**
     * Restituisce il tipo di stampa della foto.
     * @return "chiaro" o "opaco"
     */
    
    public String getTipoStampa() {
        
        return this.tipoStampa;
    }

    /**
     * Imposta il tipo di stampa della foto.
     * @param tipoStampa "chiaro" o "opaco" (case-insensitive)
     * @throws IllegalArgumentException se il valore è nullo, vuoto o non ammesso
     */
    
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