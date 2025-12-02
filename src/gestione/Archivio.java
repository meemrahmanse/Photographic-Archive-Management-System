// Archivio.java - meem

package progettoarchivio;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Rappresenta un archivio fotografico gestito da un Responsabile.
 * Contiene fotografie identificate univocamente tramite ID.
 */

public class Archivio {

    private String nomeArchivio;
    private Responsabile responsabile;
    private Map<String, Fotografia> fotografie;

//costruttore principale
public Archivio(String nomeArchivio, Responsabile responsabile) {
        
        if (nomeArchivio == null || nomeArchivio.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Il nome dell'archivio non può essere vuoto!");
        }
        this.nomeArchivio = nomeArchivio.trim();
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
    
    if (foto == null){
        
        throw new IllegalArgumentException("La fotografia non può essere vuota!");
    }

    String id = foto.getIdFoto();
    
    if (id == null || id.trim().isEmpty()) {
        
            throw new IllegalArgumentException("La fotografia deve avere un ID valido!");
        }
    id = id.trim();
    
    if (fotografie.containsKey(id)) {
        
        throw new IllegalArgumentException("La fotografia con ID ' " + foto.getIdFoto() + "' esiste gia!");
    }
    fotografie.put(id, foto);
}

    /**
     * Rimuove una fotografia dato il suo ID.
     * @return true se la foto è stata rimossa, false se non trovata.
     */

    public Fotografia rimuoviFoto(String idFoto) {
        
        if (idFoto == null || idFoto.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID della fotografia non può essere vuoto!");
        }

        Fotografia rimossa = fotografie.remove(idFoto.trim());

        if (rimossa == null) {
            
            throw new IllegalArgumentException("Nessuna fotografia trovata con ID '" + idFoto + "'!");
        }

        return rimossa;
    }

    /**
     * Cerca una fotografia per ID.
     * @return la fotografia trovata oppure null se non esiste.
     */

    public Fotografia cercaFoto(String idFoto) {
        
        if (idFoto == null) {
            
            return null;
        }
        return fotografie.get(idFoto.trim());
    }
    //getters
    
    public String getNomeArchivio() {
        
        return nomeArchivio;
    }

    public Responsabile getResponsabile() {
        
        return responsabile;
    }
    
    public List<Fotografia> getFotografie() {
        
        return Collections.unmodifiableList(new ArrayList<>(fotografie.values())); // restituisce una copia per sicurezza
    }

    
    @Override
    public String toString() {
        
        return "Archivio: " + nomeArchivio + " (foto: " + fotografie.size() + ")" + ", Responsabile: " + responsabile;
    }
}



// this class represents an archive that contains photographs and is managed by a responsible person
