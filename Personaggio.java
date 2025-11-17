package progettoarchivio;

import java.util.Objects;
import java.time.Year;

/**
 * Rappresenta una persona (vivo o deceduta) catalogata nell'archivio fotografico.
 * Estende Soggetto con chiave univoca alfanumerica.
 */

public class Personaggio extends Soggetto {
    
    private final String nome;  
    private final Genere sesso; // m, f, a    
    private final int nascita;    
    private final boolean morte;
    
   /**
     * Costruisce Personaggio.
     * @param key = chiave univoca (validata in Soggetto)
     * @param nome = nome completo 
     * @param sesso = 'M', 'F' o 'A'
     * @param morte = true se deceduto
     * @param nascita anno di nascita (0 <= anno <= anno corrente)
     * @throws IllegalArgumentException se parametri invalidi
     */

    public Personaggio(String key, String nome, Genere sesso, boolean morte, int nascita) {
        
        super(key);
        
        this.nome = validaNome(nome);   
        this.sesso = sesso;
        this.nascita = validaNascita(nascita);
        this.morte = morte;      

    }

    private static String validaNome(String name) {
        
        if (name == null || name.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Questo campo è obbligatorio, per favore inserisca il nome!");
        }
        return name.trim();
    }

    
    private static int validaNascita(int anno) {
        
        int annoCorrente = Year.now().getValue();
        
        if (anno < 0 || anno > annoCorrente) {
            
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
        
        return String.format("%s - %s", super.toString(), getDescription());
    }
    
  
