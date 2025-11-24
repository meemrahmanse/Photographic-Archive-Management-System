package progettoarchivio;

import java.time.Year;

/**
 * Rappresenta un'opera d'arte fotografata.
 * Estende Soggetto con chiave univoca.
 */

public class OperaArte extends Soggetto {
    
    private String nomeOpera;  
    private String artista;   
    private String luogo;
    private int anno;

    /**
     * Costruisce OperaArte.
     * @param key = chiave univoca (validata in Soggetto)
     * @param nomeOpera = nome dell'opera 
     * @param artista = nome dell'artista 
     * @param luogo = luogo dell'opera 
     * @param anno = anno di creazione 
     * @throws IllegalArgumentException se parametri non validi
     */
    
    public OperaArte(String key, String nomeOpera, String artista, String luogo, int anno) {
        
        super(key);
        
        this.nomeOpera = validaNomeOpera(nomeOpera);
        this.artista = validaArtista(artista);
        this.luogo = validaLuogo(luogo);
        this.anno = validaAnno(anno);
    }

    public OperaArte (){}

    private String validaNomeOpera(String nome) {
        
        if (nome == null || nome.trim().isEmpty()){
            
            throw new IllegalArgumentException("Inserire il nome dell'opera è obbligatorio!");
        }
        return nome.trim();
    }

    private String validaArtista(String artista) {
        
        if (artista == null || artista.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Inserire il nome dell'artista è obbligatorio!");
        }
        return artista.trim();
    }

    private String validaLuogo(String luogo) {
        
        if (luogo == null || luogo.trim().isEmpty()){
            
            throw new IllegalArgumentException("Inserire il nome del luogo è obbligatorio!");
        }
        return luogo.trim();
    }

    private int validaAnno(int anno) {
        
        int annoCorrente = Year.now().getValue();
        
        if (anno < 0 || anno > annoCorrente){
            
            throw new IllegalArgumentException(String.format("L'anno inserito non è valido: %d. Perfavore inserisca un valore tra 0 e %d", anno, annoCorrente));
        }
        return anno;
    }

    //getters
    
    public String getNomeOpera() { 
        
        return nomeOpera;
    }
    
    public String getArtista() { 
        
        return artista; 
    }
    
    public String getLuogo() { 
        
        return luogo; 
    }
    
    public int getAnno() {
        
        return anno; 
    }

    @Override
    
    public String getDescription() {
        
        return String.format("%s di %s (%d), %s", nomeOpera, artista, anno, luogo);
    }

    @Override
    
    public String toString() {
        
        return String.format("%s - %s", super.toString(), getDescription());
    }

}
