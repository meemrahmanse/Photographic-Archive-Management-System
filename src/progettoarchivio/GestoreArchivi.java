// GestoreArchivi.java - meem

package progettoarchivio;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * GestoreArchivi è un singleton che gestisce più archivi fotografici.
 * Permette di aggiungere, modificare, eliminare e salvare/caricare archivi da file JSON.
 */

public class GestoreArchivi {

    private static volatile GestoreArchivi instance; //thread-safe singleton
    private Map<String, Archivio> archivi;
    private static final String NOME_FILE = "archivio.json";

    public List<Fotografia> getTutteLeFotografie() {

        List<Fotografia> tutte = new ArrayList<>();

        for (Archivio a : archivi.values()) {
            tutte.addAll(a.getFotografie());
        }

        return tutte;
    }

    
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

    public void aggiungiFotografia(String nomeArchivio, Fotografia f)
            throws ArchivioException {

        // Controllo oggetto nullo
        if (f == null) {
            throw new ArchivioException("Fotografia nulla.");
        }

        // Controllo archivio esistente
        Archivio archivio = archivi.get(nomeArchivio);

        if (archivio == null) {
            throw new ArchivioException("Archivio non trovato.");
        }

        // Controllo ID
        if (f.getIdFoto() == null || f.getIdFoto().isBlank()) {
            throw new ArchivioException("ID non valido.");
        }

        if (archivio.cercaFoto(f.getIdFoto()) != null) {
            throw new ArchivioException("ID già esistente.");
        }

        // Controllo campi obbligatori
        if (f.getTitolo() == null || f.getTitolo().isBlank()) {
            throw new ArchivioException("Titolo obbligatorio.");
        }

        if (f.getAutore() == null || f.getAutore().isBlank()) {
            throw new ArchivioException("Autore obbligatorio.");
        }

        if (f.getData() == null) {
            throw new ArchivioException("Data obbligatoria.");
        }

        // 5️⃣ Se tutto ok → modifica stato
        archivio.aggiungiFoto(f);
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
        

    } catch (IOException e) {
        
        
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
    
    
    } catch (Exception e) {


            // Backup del file danneggiato
        file.renameTo(new File("archivio_corrotto_backup.json"));
        archivi = new HashMap<>();
        
        }
    }
}



// this class manages multiple archives, allowing saving and loading from a file.
