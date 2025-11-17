package progettoarchivio;

/**
 * Rappresenta un luogo fisico fotografato.
 * Estende Soggetto con chiave univoca.
 */

public class Luogo extends Soggetto {
    
    private final String nome;
    private final String descrizione;
    
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

    private String validaNome(String nome) {
        
        if (nome == null || nome.trim().isEmpty()) {
            
            throw new IllegalArgumentException("\nQuesto campo è obbligatorio, perfavore inserisce il nome del luogo!");
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
