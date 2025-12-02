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
    private static final String NOME_FILE = "archivio.json";

//costruttore privato
private GestoreArchivi() {
    
        archivi = new HashMap<>();
        caricaDaFile();
}

/**
* Restituisce l'istanza unica del gestore.
*/
    public static GestoreArchivi getInstance() {
        
        if (instance == null) {
            
            synchronized (GestoreArchivi.class) {
                if (instance == null){
                    instance = new GestoreArchivi();
                }
            }
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
        
        String nome = archivio.getNomeArchivio().trim();
        
    if (archivi.containsKey(nome)) {
            throw new IllegalArgumentException("Esiste già un archivio chiamato: " + nome);
        }

        archivi.put(nome, archivio);
        salvaSuFile();
    }

/**
* Elimina un archivio dato il nome.
*/
    public void eliminaArchivio(String nomeArchivio) {
        
    if (nomeArchivio == null || nomeArchivio.isEmpty()) {
        
            throw new IllegalArgumentException("Nome archivio non valido!");
        }
    
    if (!archivi.containsKey(nomeArchivio)) {
            throw new IllegalArgumentException("Archivio '" + nomeArchivio + "' non esiste!");
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
    
    if (!archivi.containsKey(nomeArchivio)) {
            throw new IllegalArgumentException("Impossibile modificare: l’archivio '" + nomeArchivio + "' non esiste.");
        }
    
    archivi.put(nomeArchivio, archivioModificato);
    salvaSuFile();
}
/**
* Restituisce un archivio dato il nome.
*/

public Archivio getArchivio(String nomeArchivio) {
    
        if (nomeArchivio == null || nomeArchivio.trim().isEmpty()) {
            
            throw new IllegalArgumentException("Nome archivio non valido!");
        }
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
        
        System.err.println("Errore: salvataggio JSON fallito: " + e.getMessage());
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
        
        Map<String, Archivio> caricati = gson.fromJson(reader, tipo);
        
        if (caricati == null){
            
            throw new IOException("Il file JSON è corrotto o vuoto!");
        }
        
        archivi = caricati;
        System.out.println("Dati caricati con successo da " + NOME_FILE);
    
    } catch (Exception e) {
        
        System.err.println("[ERRORE] Lettura JSON fallita: " + e.getMessage());
        System.err.println("[ATTENZIONE] Creazione nuovo archivio + backup del file corrotto.");

            // Backup del file danneggiato
        file.renameTo(new File("archivio_corrotto_backup.json"));
        archivi = new HashMap<>();
        
        }
    }
}



// this class manages multiple archives, allowing saving and loading from a file.
