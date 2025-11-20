/**
    *Politico con partito e carica governativa.
*/
package progettoarchivio;

public class Politico extends Personaggio {
    
    private final String partito;
    
    private final String carica; // opzionale

    public Politico(String key, String nome, char sesso, boolean morte, int nascita, String partito, String carica) {
        
        super(key, nome, sesso, morte, nascita);
        
        this.partito = validaPartito(partito);
        
        this.carica = carica != null ? carica.trim() : "";  //Se carica non è nulla, la pulisco con trim(), altrimenti metto stringa vuota. Così evito errori e gestisco il campo opzionale
    }

   public Politico(){}

    private String validaPartito(String partito) {
        
        if (partito == null || partito.trim().isEmpty()) {
            
            throw new IllegalArgumentException("");
        }
        return partito.trim();
    }

    public String getPartito() { 
        
        return partito; 
    }
    
    public String getCarica() { 
        
        return carica.isEmpty() ? "Nessuna" : carica;     //scrive nessuna se l'utente lascia vuoto
    
    }

    @Override
    
    public String getDescription() {
        
        return super.getDescription() + " - " + partito + (carica.isEmpty() ? "" : ", " + carica);
    }

}

