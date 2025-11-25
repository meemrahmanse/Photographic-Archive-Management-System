// Archivio.java - meem

package progettoarchivio;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Archivio {

    private String nomeArchivio;
    private Responsabile responsabile;
    private Map<String, Fotografia> fotografie;

//costruttore principale
public Archivio(String nomeArchivio, Responsabile responsabile) {

        this.nomeArchivio = Objects.requireNonNull(nomeArchivio, "Il nome dell'archivio non può essere vuoto!");
        this.responsabile = Objects.requireNonNull(responsabile, "Il responsabile non può essere vuoto!");
        this.fotografie = new HashMap<>();
}

//costruttore vuoto per caricamenti da file
public Archivio() {
        
        this.nomeArchivio = "";
        this.responsabile = null;
        this.fotografie = new HashMap<>();
}
/**
    * Aggiunge una fotografia all'archivio.
    * Se l'ID è già presente, non la inserisce per evitare duplicati.
*/

public void aggiungiFoto(Fotografia foto) {
    
    if (foto == null || foto.getIdFoto() == null || foto.getIdFoto().isEmpty()){
        
        throw new IllegalArgumentException("La fotografia deve avere un ID valido!");
    }

    if (fotografie.containsKey(foto.getIdFoto())) {
        
        throw new IllegalArgumentException("La fotografia con ID ' " + foto.getIdFoto() + "' esiste gia!");
    }


    fotografie.put(foto.getIdFoto(), foto);
}

    /**
     * Rimuove una fotografia dato il suo ID.
     * @return true se la foto è stata rimossa, false se non trovata.
     */

    public boolean rimuoviFoto(String idFoto) {
        
        return fotografie.remove(idFoto) != null;
    }

    /**
     * Cerca una fotografia per ID.
     * @return la fotografia trovata oppure null se non esiste.
     */

    public Fotografia cercaFoto(String idFoto) {
        
        return fotografie.get(idFoto);
    }
    //getters
    
    public String getNomeArchivio() {
        
        return nomeArchivio;
    }

    public Responsabile getResponsabile() {
        
        return responsabile;
    }
    
    public List<Fotografia> getFotografie() {
        
        return new ArrayList<>(fotografie.values()); // restituisce una copia per sicurezza
    }

    
    @Override
    public String toString() {
        
        return "Archivio: " + nomeArchivio + " (foto: " + fotografie.size() + ")" + ", Responsabile: " + responsabile;
    }
}



// this class represents an archive that contains photographs and is managed by a responsible person
