package model;

import java.util.Objects;
import java.time.LocalDate;
        
/**
 * Rappresenta una fotografia con i suoi attributi principali:
 * ID univoco, dimensioni, stato di conservazione, soggetto fotografato,
 * titolo, autore e data di scatto.
 */

public class Fotografia {

    private String idFoto;
    private int altezza;
    private int larghezza;
    private StatoConservazione statoConservazione;
    private Soggetto soggetto;
    private String titolo;
    private String autore;
    private LocalDate data;

    /**
     * Costruisce una fotografia con tutti i dati obbligatori.
     * @param idFoto              identificativo univoco
     * @param altezza             altezza in pixel (deve essere positiva)
     * @param larghezza           larghezza in pixel (deve essere positiva)
     * @param statoConservazione  stato di conservazione
     * @param soggetto            soggetto fotografato
     * @param titolo              titolo della fotografia
     * @param autore              autore della fotografia
     * @param data                data di scatto
     * @throws IllegalArgumentException se uno dei parametri non è valido
     */
    
    public Fotografia(String idFoto, int altezza, int larghezza, StatoConservazione statoConservazione, Soggetto soggetto, String titolo, String autore, LocalDate data) {

        setIdFoto(idFoto);
        setAltezza(altezza);
        setLarghezza(larghezza);
        this.statoConservazione = Objects.requireNonNull(statoConservazione,"Lo stato di conservazione non può essere nullo!");
        this.soggetto = Objects.requireNonNull(soggetto,"Il soggetto non può essere nullo!");
        
        if (titolo == null || titolo.isBlank()) {
        	
            throw new IllegalArgumentException("Titolo obbligatorio.");
        }
        
        if (autore == null || autore.isBlank()) {
        	
            throw new IllegalArgumentException("Autore obbligatorio.");
        }
        
        if (data == null) {
        	
            throw new IllegalArgumentException("Data obbligatoria.");
        }
        
        this.titolo = titolo.trim();
        this.autore = autore.trim();
        this.data = data;
    }
    
    /**
     * Costruttore vuoto necessario per la deserializzazione da file JSON.
     */
    
    public Fotografia() {}

    
    public String getIdFoto() {
        
        return idFoto;
    }

    /**
     * Imposta l'ID della fotografia.
     * @param idFoto identificativo univoco (non può essere nullo o vuoto)
     * @throws IllegalArgumentException se l'ID non è valido
     */
    
    public void setIdFoto(String idFoto) {
        
        if (idFoto == null || idFoto.trim().isEmpty()){
        
            throw new IllegalArgumentException("ID foto non valido!");
        }
        this.idFoto = idFoto.trim();
    }


    public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public int getAltezza() {
        
        return altezza;
    }

    public int getLarghezza(){
        
        return larghezza;
    }

    /**
     * Imposta l'altezza della fotografia.
     * @param altezza altezza in pixel (deve essere positiva)
     * @throws IllegalArgumentException se il valore è minore o uguale a zero
     */
    
    public void setAltezza(int altezza) {
        
        if (altezza <= 0) {
            
            throw new IllegalArgumentException("L'altezza deve essere positiva!");
        }
        this.altezza = altezza;
    }

    public void setLarghezza(int larghezza) {
        
        if (larghezza <= 0) {
            
            throw new IllegalArgumentException("La larghezza deve essere positiva!");
        }
        this.larghezza = larghezza;
    }
    
    public StatoConservazione getStatoConservazione() {
        
        return statoConservazione;
    }

    /**
     * Imposta lo stato di conservazione da stringa (case-insensitive).
     * @param statoConservazione stringa corrispondente a un valore dell'enum
     * @throws IllegalArgumentException se il valore non è riconosciuto
     */
    
    public void setStatoConservazione(String statoConservazione) {
        
    if (statoConservazione == null || statoConservazione.isEmpty()) {
        
        throw new IllegalArgumentException("Stato di conservazione non valido!");
    }
    
    try {
        
        this.statoConservazione = StatoConservazione.valueOf(statoConservazione.trim().toUpperCase());
    
    } catch (IllegalArgumentException e) {
        
        throw new IllegalArgumentException("Valore non riconosciuto: " + statoConservazione + ". Valori ammessi: BUONO, DANNEGGIATO, PESSIMO, RESTAURATO.");
    }
}

        public void setStatoConservazione(StatoConservazione statoConservazione) {
            
        this.statoConservazione = Objects.requireNonNull(statoConservazione,"Lo stato di conservazione non può essere nullo!");
    }

    public Soggetto getSoggetto() {
        
        return soggetto;
    }


    public void setSoggetto(Soggetto soggetto) {
        this.soggetto = Objects.requireNonNull(soggetto,"Il soggetto non può essere nullo!");
    }

    
    @Override
    
    public String toString() {
        return "Fotografia [ID: " + idFoto + ", Dimensione: " + altezza + " x " + larghezza + ", Stato: " + statoConservazione + ", Soggetto: " + (soggetto != null ? soggetto : "N/D") + "]";
    }
    
    /**
     * Due fotografie sono uguali se hanno lo stesso ID.
     */
    
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

    /**
     * Il codice hash è basato sull'ID della fotografia.
     */
    
    @Override
    
    public int hashCode() {
        
        return idFoto != null ? idFoto.hashCode() : 0;
    }
}