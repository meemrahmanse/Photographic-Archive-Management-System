// GestoreArchivi.java - meem

package gestione;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


// this class manage diffrent archives, saving and loading from files.
public class GestoreArchivi {

    private static GestoreArchivi instance;
    private Map<String, Archivio> archivi;
    private final String NOME_FILE = "archivio.json";

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

    //this is a method to add an archive to the manager.
    public void aggiungiArchivio(Archivio archivio) {
        archivi.put(archivio.getNomeArchivio(), archivio);
        salvaSuFile();
    }

    public void eliminaArchivio(String nomeArchivio) {
    archivi.remove(nomeArchivio);
    salvaSuFile();
}

public void modificaArchivio(String nomeArchivio, Archivio archivioModificato) {
    archivi.put(nomeArchivio, archivioModificato);
    salvaSuFile();
}
    // method to get an archive by name
    public Archivio getArchivio(String nomeArchivio) {
        return archivi.get(nomeArchivio);
    }

    public Map<String, Archivio> getArchivi() {
        return archivi;
    }

    // method to save the archives to a file
    public void salvaSuFile() {
    try (Writer writer = new FileWriter(NOME_FILE)) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(archivi, writer);
        System.out.println("Dati salvati con successo su archivi.json");
    } catch (IOException e) {
        System.err.println("Errore durante il salvataggio JSON: " + e.getMessage());
    }
}

    // method to load the archives from file
    @SuppressWarnings("unchecked")
   public void caricaDaFile() {
    File file = new File("archivi.json");
    if (!file.exists()) {
        archivi = new HashMap<>();
        return;
    }

    try (Reader reader = new FileReader(file)) {
        Gson gson = new Gson();
        Type tipo = new TypeToken<HashMap<String, Archivio>>() {}.getType();
        archivi = gson.fromJson(reader, tipo);
        if (archivi == null) archivi = new HashMap<>();
        System.out.println("Dati caricati con successo da archivi.json");
    } catch (IOException e) {
        System.err.println("Errore durante il caricamento JSON: " + e.getMessage());
        archivi = new HashMap<>();
    }
}
}



// this class manages multiple archives, allowing saving and loading from a file.
