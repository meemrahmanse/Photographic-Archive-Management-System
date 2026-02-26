package model;

/**
 * Rappresenta un luogo fisico fotografato.
 * Estende Soggetto con chiave univoca.
 */

public class Luogo extends Soggetto {
    
    private String nome;
    private String descrizione;
    
    /**
     * Costruisce un Luogo con chiave, nome e descrizione opzionale.
     * @param key         chiave univoca (validata in Soggetto)
     * @param nome        nome del luogo
     * @param descrizione descrizione opzionale (può essere nulla)
     * @throws IllegalArgumentException se il nome è nullo o vuoto
     */

    public Luogo (String key, String nome, String descrizione) {
        
        super(key);
        
        this.nome = validaNome(nome);
        this.descrizione = descrizione != null ? descrizione.trim() : "";
    }

    /**
     * Costruttore vuoto necessario per la deserializzazione da file JSON.
     */
    
    public Luogo (){}

    /**
     * Valida e normalizza il nome del luogo.
     * @param nome nome da validare
     * @return nome trimmed
     * @throws IllegalArgumentException se il nome è nullo o vuoto
     */
    
    private String validaNome(String nome) {
        
        if (nome == null || nome.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Errore: il nome del luogo è obbligatorio!");
        }
        return nome.trim();
    }

    /**
     * Restituisce il nome del luogo.
     * @return nome del luogo
     */
    
    public String getNome() { 
        
        return nome; 
    }
    
    /**
     * Restituisce la descrizione del luogo.
     * @return descrizione, oppure stringa vuota se non presente
     */
    
    public String getDescrizione() {
        
        return descrizione;
    }
    
    @Override
    public String getDescription() {
        
        return descrizione.isEmpty()
                ? nome
                : nome + " - " + descrizione;
    }
    
    /**
     * Restituisce una descrizione leggibile del luogo.
     * Se presente, include anche la descrizione separata da " - ".
     * @return nome del luogo, eventualmente seguito dalla descrizione
     */
    
    @Override
    public String toString() {
        return String.format("%s - %s", super.toString(), getDescrizione());
    }

}

