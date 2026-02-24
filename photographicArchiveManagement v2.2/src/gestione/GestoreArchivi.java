// GestoreArchivi.java - meem

package gestione;

import database.JsonDatabase;
import java.util.HashMap;
import java.util.Map;

// this class manage diffrent archives, saving and loading from JSON files.
public class GestoreArchivi {

    private static GestoreArchivi instance;
    private Map<String, Archivio> archivi;

    private GestoreArchivi() {
        archivi = new HashMap<>();
        caricaDaFile();
    }

    // this is a singleton pattern to shure one instance only.
    public static synchronized GestoreArchivi getInstance() {
        if (instance == null) {
            instance = new GestoreArchivi();
        }
        return instance;
    }

    // this is a method to add an archive to the manager.
    public void aggiungiArchivio(Archivio archivio) {
        archivi.put(archivio.getNomeArchivio(), archivio);
    }

    // method to get an archive by name
    public Archivio getArchivio(String nomeArchivio) {
        return archivi.get(nomeArchivio);
    }

    // method to remove an archive
    public boolean rimuoviArchivio(String nomeArchivio) {
        if (archivi.containsKey(nomeArchivio)) {
            archivi.remove(nomeArchivio);
            return true;
        }
        return false;
    }

    public Map<String, Archivio> getArchivi() {
        return archivi;
    }

    // method to save the archives to JSON file
    public void salvaSuFile() {
        JsonDatabase.salvaArchivi(archivi);
    }

    // method to load the archives from JSON file
    public void caricaDaFile() {
        archivi = JsonDatabase.caricaArchivi();
        if (archivi == null) {
            archivi = new HashMap<>();
        }
    }
}

// this class manages multiple archives, allowing saving and loading from a JSON
// file.