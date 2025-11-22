// Archivio.java - meem

package progettoarchivio;

import java.util.ArrayList;
import java.util.List;


public class Archivio {

    private String nomeArchivio;
    private Responsabile responsabile;
    private List<Fotografia> fotografie;

//costruttore principale
public Archivio(String nomeArchivio, Responsabile responsabile) {

        this.nomeArchivio = nomeArchivio;
        this.responsabile = responsabile;
        this.fotografie = new ArrayList<>();
}

//costruttore vuoto per caricamenti da file
public Archivio() {
        
        this.fotografie = new ArrayList<>();
}
/**
    * Aggiunge una fotografia all'archivio.
    * Se l'ID è già presente, non la inserisce per evitare duplicati.
*/
public void aggiungiFoto(Fotografia foto) {

    if (foto == null) {
        
            throw new IllegalArgumentException("La foto non può essere vuota!");
        }
        if (cercaFoto(foto.getIdFoto()) != null) {
            
            throw new IllegalArgumentException("La foto con ID '" + foto.getIdFoto() + "' è già presente nell'archivio!");
        }
        this.fotografie.add(foto);
    }

    /**
     * Rimuove una fotografia dato il suo ID.
     * @return true se la foto è stata rimossa, false se non trovata.
     */

    public boolean rimuoviFoto(String idFoto) {
        
        if (idFoto == null || idFoto.isEmpty()) {
            
            return false;
        }
        return this.fotografie.removeIf(foto -> idFoto.equals(foto.getIdFoto()));
    }

    /**
     * Cerca una fotografia per ID.
     * @return la fotografia trovata oppure null se non esiste.
     */

    public Fotografia cercaFoto(String idFoto) {
        
        if (idFoto == null || idFoto.isEmpty()) {
            return null;
        }
        
        for (Fotografia foto : fotografie) {
            
            if (idFoto.equals(foto.getIdFoto())) {
                
                return foto;
            }
        }
        return null;
    }

    //getters
    
    public String getNomeArchivio() {
        
        return nomeArchivio;
    }

    public Responsabile getResponsabile() {
        
        return responsabile;
    }
    
    public List<Fotografia> getFotografie() {
        
        return new ArrayList<>(fotografie); // restituisce una copia per sicurezza
    }

    
    @Override
    public String toString() {
        
        return "Archivio: " + nomeArchivio + " (foto: " + fotografie.size() + ")" + ", Responsabile: " + responsabile;
    }
}



// this class represents an archive that contains photographs and is managed by a responsible person.
