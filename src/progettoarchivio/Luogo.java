package progettoarchivio;

/**
 * Rappresenta un luogo fisico fotografato.
 * Estende Soggetto con chiave univoca.
 */

public class Luogo extends Soggetto {
    
    private String nome;
    private String descrizione;
    
/**
     * Costruisce Luogo
     * @param key chiave univoca (validata in Soggetto)
     * @param nome nome luogo 
     * @param descrizione = descrizione opzionale 
     * @throws IllegalArgumentException se nome è null o vuoto
*/

    public Luogo (String key, String nome, String descrizione) {
        
        super(key);
        
        this.nome = validaNome(nome);
        this.descrizione = descrizione != null ? descrizione.trim() : "";
    }

    public Luogo (){}

    private String validaNome(String nome) {
        
        if (nome == null || nome.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Erore: il nome del luogo è obbligatorio!");
        }
        return nome.trim();
    }

    public String getNome() { 
        
        return nome; 
    }
    
    public String getDescrizione() {
        
        return descrizione;
    }
    
    @Override
    public String getDescription() {
        
        return descrizione.isEmpty()
                ? nome
                : nome + " - " + descrizione;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s", super.toString(), getDescrizione());
    }

}

