// GestoreArchivi.java - meem

package gestione;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


// this class manage diffrent archives, saving and loading from files.
public class GestoreArchivi {

    private static GestoreArchivi instance;
    private Map<String, Archivio> archivi;
    private final String NOME_FILE = "archivio.dat";

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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_FILE))) {
            oos.writeObject(archivi);
            System.out.println("Dati salvati con successo su " + NOME_FILE);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dei dati: " + e.getMessage());
        }
    }

    // method to load the archives from file
    @SuppressWarnings("unchecked")
    public void caricaDaFile() {
        File file = new File(NOME_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOME_FILE))) {
                archivi = (HashMap<String, Archivio>) ois.readObject();
                System.out.println("Dati caricati con successo da " + NOME_FILE);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Errore durante il caricamento dei dati: " + e.getMessage());
                archivi = new HashMap<>(); // In caso di errore, inizia con una mappa vuota
            }
        }
    }
}


// this class manages multiple archives, allowing saving and loading from a file.