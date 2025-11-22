// GestoreArchivi.java - meem

package progettoarchivio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * GestoreArchivi è un singleton che gestisce più archivi fotografici.
 * Permette di aggiungere, modificare, eliminare e salvare/caricare archivi da file JSON.
 */

public class GestoreArchivi {

    private static volatile GestoreArchivi instance; //thread-safe singleton
    private Map<String, Archivio> archivi;
    private final String NOME_FILE = "archivio.json";

//costruttore privato
private GestoreArchivi() {
    
        archivi = new HashMap<>();
        caricaDaFile();
}

/**
* Restituisce l'istanza unica del gestore.
*/
    public static synchronized GestoreArchivi getInstance() {
        
        if (instance == null) {
            
            instance = new GestoreArchivi();
        }
        return instance;
    }

/**
* Aggiunge un archivio al gestore.
* Se esiste già un archivio con lo stesso nome, viene sovrascritto.
*/
    
    public void aggiungiArchivio(Archivio archivio) {
        
        if (archivio == null || archivio.getNomeArchivio() == null || archivio.getNomeArchivio().isEmpty()) {
            
            throw new IllegalArgumentException("Archivio non valido: nome mancante o archivio nullo!");
        }
        archivi.put(archivio.getNomeArchivio(), archivio);
        salvaSuFile();
    }

/**
* Elimina un archivio dato il nome.
*/
    public void eliminaArchivio(String nomeArchivio) {
        
    if (nomeArchivio == null || nomeArchivio.isEmpty()) {
        
            throw new IllegalArgumentException("Nome archivio non valido!");
        }
    
        archivi.remove(nomeArchivio);
        salvaSuFile();
    }
    
/**
* Modifica un archivio esistente sostituendolo con uno nuovo.
*/
    
public void modificaArchivio(String nomeArchivio, Archivio archivioModificato) {
    
    if (nomeArchivio == null || nomeArchivio.isEmpty() || archivioModificato == null) {
        
            throw new IllegalArgumentException("Parametri non validi per la modifica dell'archivio.");
        }
    archivi.put(nomeArchivio, archivioModificato);
    salvaSuFile();
}
/**
* Restituisce un archivio dato il nome.
*/

public Archivio getArchivio(String nomeArchivio) {
    
        return archivi.get(nomeArchivio);
}

    /**
     * Restituisce una copia della mappa degli archivi.
     */

public Map<String, Archivio> getArchivi() {
        
        return new HashMap<>(archivi);
}

    /**
     * Salva gli archivi su file JSON.
     */

public void salvaSuFile() {
    
    try (Writer writer = new FileWriter(NOME_FILE)) {
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        gson.toJson(archivi, writer);
        
        System.out.println("Dati salvati con successo su " + NOME_FILE);
    } catch (IOException e) {
        
        System.err.println("Errore durante il salvataggio JSON: " + e.getMessage());
    }
}

     /**
     * Carica gli archivi da file JSON.
     */
    public void caricaDaFile() {
        
        File file = new File(NOME_FILE);

        if (!file.exists()) {
            
            archivi = new HashMap<>();
            return;
        }

    try (Reader reader = new FileReader(file)) {
        
        Gson gson = new Gson();
        Type tipo = new TypeToken<HashMap<String, Archivio>>() {}.getType();
        
        archivi = gson.fromJson(reader, tipo);
        
        if (archivi == null){
            
            archivi = new HashMap<>();
        }
        System.out.println("Dati caricati con successo da " + NOME_FILE);
    
    } catch (Exception e) {
        
        System.err.println("Errore durante il caricamento JSON: " + e.getMessage());
        archivi = new HashMap<>();
        
        }
    }
}



// this class manages multiple archives, allowing saving and loading from a file.
