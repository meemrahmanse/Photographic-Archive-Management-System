package progettoarchivio;

/**
 * Rappresenta un oggetto inanimato fotografato
 * Estende Soggetto con chiave univoca.
 */

public class Oggetto extends Soggetto {
    
    private final String nome;
    private final String descrizione;
    
    /**
     * Costruisce Oggetto.
     * @param key chiave univoca (validata in Soggetto)
     * @param nome nome oggetto 
     * @param descrizione = descrizione opzionale 
     * @throws IllegalArgumentException se nome è null o vuoto
     */

    public Oggetto(String key, String nome, String descrizione) {
        
        super(key);
        
        this.nome = validaNome(nome);
        this.descrizione = descrizione != null ? descrizione.trim() : "";
    }

    public Oggetto (){}

    private String validaNome(String nome) {
        
        if (nome == null || nome.trim().isEmpty()){
            
            throw new IllegalArgumentException("\nQuesto campo è obbligatorio, per favore inserisca il nome dell'oggetto!");
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
        
        return String.format("%s - %s", super.toString(), getDescription());
    }

}
