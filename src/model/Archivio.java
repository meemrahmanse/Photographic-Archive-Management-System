package model;

import java.util.Map;
import java.util.Objects;

import model.Responsabile;

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
 * @param foto la fotografia da aggiungere
 * @throws IllegalArgumentException se la foto è nulla, ha ID non valido, o l'ID è già presente
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
 * @param idFoto l'ID della fotografia da rimuovere
 * @return la fotografia rimossa, oppure null se non trovata
 */

public Fotografia rimuoviFoto(String idFoto) {
    return fotografie.remove(idFoto);
}

/**
 * Sovrascrive una fotografia esistente con i nuovi dati.
 * @param foto la fotografia aggiornata (deve avere un ID già presente)
 */

public void aggiornaFoto(Fotografia foto) {
    fotografie.put(foto.getIdFoto(), foto);
}


/**
 * Cerca una fotografia per ID.
 * @param idFoto l'ID della fotografia da cercare
 * @return la fotografia trovata, oppure null se non esiste
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