package progettoarchivio;

import java.util.Objects;
import java.time.Year;

/**
 * Rappresenta una persona (viva o deceduta) catalogata nell'archivio fotografico.
 * Estende Soggetto con chiave univoca alfanumerica.
 */

public class Personaggio extends Soggetto {
    
    private final String nome;  
    private final Genere sesso; // m, f, a    
    private final int nascita;    
    private final boolean morte;
    private static final int MIN_ANNO = 0;
    private static final int ANNO_CORRENTE = Year.now().getValue();
   /**
     * Costruisce Personaggio con tutti i dati anagrafici
     * @param key = chiave univoca (validata in Soggetto)
     * @param nome = nome completo 
     * @param sesso = 'M', 'F' o 'A'
     * @param morte = true se deceduto
     * @param nascita anno di nascita (1 <= anno <= anno corrente)
     * @throws IllegalArgumentException se parametri invalidi
     * * @throws NullPointerException     se sesso è null
     */

    public Personaggio(String key, String nome, Genere sesso, boolean morte, int nascita) {
        
        super(key);
        
        this.nome = validaNome(nome);   
        this.sesso = sesso = Objects.requireNonNull(sesso, "E' obbligatorio inserire il sesso!");
        this.nascita = validaNascita(nascita);
        this.morte = morte;      

    }

    public Personaggio (){}
/**
     * Valida e normalizza il nome della persona.
*/
    private static String validaNome(String name) {
        
        if (name == null) {
            
            throw new IllegalArgumentException("Il nome del personaggio è obbligatorio!");
        }
        String trimmed = name.trim();
        
        if (trimmed.isEmpty()) {
            
            throw new IllegalArgumentException("Il nome non può essere vuoto!");
        }
        return trimmed;
    }

/**
     * Valida l'anno di nascita.
*/    
    private static int validaNascita(int anno) {
        
        int annoCorrente = Year.now().getValue();
        
        if (anno < MIN_ANNO || anno > ANNO_CORRENTE) {
            
            throw new IllegalArgumentException(String.format("Anno di nascita non valido: %d. Per favore inserisca un valore tra 0 e %d", anno, annoCorrente));
        }
        return anno;
    }

    public String getNome() { 
        
        return nome; 
    }
    public Genere getSesso() {
        
        return sesso; 
    }
    public int getNascita(){
    
        return nascita;
    }
    
    /**
     * Indica se la persona è deceduta.
     * @return true se deceduta, false altrimenti
     */
    public boolean isMorte() { 
        
        return morte;
    }

    @Override
    
    public String getDescription() {
        
        String base = String.format("%s (%s, nato nel %d)", nome, sesso.getEtichetta(), nascita);
        return morte ? base + ", deceduto" : base;
    }
    
    @Override
    
    public String toString() {
        
        return super.toString() + " - " + getDescription();
    }
    
}
