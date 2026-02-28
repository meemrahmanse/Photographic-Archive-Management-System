package progettoarchivio;

import java.util.Objects;
import java.time.Year;

/**
 * Rappresenta una persona (vivo o morta)
 * Estende Soggetto con chiave univoca.
 */

public class Personaggio extends Soggetto {
    
    private static final int ANNO_CORRENTE = java.time.Year.now().getValue();
    
    private final String nome;
    
    private final Genere sesso; // m, f, a
    
    private final int nascita;
    
    private final boolean morte;
    
   /**
     * Costruisce Personaggio.
     * @param key = chiave univoca (validata in Soggetto)
     * @param nome = nome completo 
     * @param sesso 'M', 'F' o 'A'
     * @param morte = true se morto
     * @param nascita anno di nascita (0 <= anno <= anno corrente)
     * @throws IllegalArgumentException se parametri invalidi
     */

    public Personaggio(String key, String nome, char sesso, boolean morte, int nascita) {
        
        super(key);
        
        this.nome = validaNome(nome);   
        this.sesso = Genere.daChar(sesso);
        this.nascita = validaNascita(nascita);
        this.morte = morte;      

    }

    private String validaNome(String name) {
        
        if (name == null || name.trim().isEmpty()) {
            
            throw new IllegalArgumentException("\nQuesto campo è obbligatorio, perfavore inserisca il nome!");
        }
        return name.trim();
    }

    
    private int validaNascita(int anno) {
        
        if (anno < 0 || anno > ANNO_CORRENTE) {
            
    throw new IllegalArgumentException(String.format("L'nno di nascita è invalido: %d. Perfavore inserisca un valore tra 0 e %d.", anno, ANNO_CORRENTE));
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
        
        String base = String.format("%s (%s, nato nel %d)", nome, sesso, nascita);
        return morte ? base + ", deceduto" : base;
    }
    
    @Override
    
    public String toString() {
        
        return String.format("%s - %s", super.toString(), getDescription());
    }
    
    @Override
    
    public boolean equals(Object o) {
        
        if (this == o){
            
            return true;
        }
        
        if (!(o instanceof Personaggio p)){
            
            return false;
        }
        return getKey().equals(p.getKey()) &&
               nome.equals(p.nome) &&
               sesso == p.sesso &&
               nascita == p.nascita &&
               morte == p.morte;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), nome, sesso, nascita, morte);
    }

}


//?: if else
