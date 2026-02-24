// JsonDatabase.java - meem
// JSON database utility class using Gson for serialization/deserialization

package database;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import gestione.*;
import progettoarchivio.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Utility class for JSON database operations.
 * Handles saving and loading data in JSON format.
 */
public class JsonDatabase {

    private static final String DATA_DIR = "data";
    private static final String ARCHIVI_FILE = DATA_DIR + "/archivi.json";
    private static final String SOGGETTI_FILE = DATA_DIR + "/soggetti.json";

    private static Gson gson;

    // Initialize Gson with custom type adapters
    static {
        gson = createGson();
    }

    /**
     * Creates a Gson instance with proper configuration for polymorphism
     */
    private static Gson createGson() {
        RuntimeTypeAdapterFactory<Soggetto> soggettoAdapter = RuntimeTypeAdapterFactory
                .of(Soggetto.class, "type")
                .registerSubtype(Personaggio.class, "Personaggio")
                .registerSubtype(Artista.class, "Artista")
                .registerSubtype(Politico.class, "Politico")
                .registerSubtype(Luogo.class, "Luogo")
                .registerSubtype(Oggetto.class, "Oggetto")
                .registerSubtype(OperaArte.class, "OperaArte");

        RuntimeTypeAdapterFactory<Fotografia> fotografiaAdapter = RuntimeTypeAdapterFactory
                .of(Fotografia.class, "type")
                .registerSubtype(Fotografia.class, "Fotografia")
                .registerSubtype(FotoAColore.class, "FotoAColore");

        return new GsonBuilder()
                .registerTypeAdapterFactory(soggettoAdapter)
                .registerTypeAdapterFactory(fotografiaAdapter)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    /**
     * Ensures data directory exists
     */
    private static void ensureDataDirectory() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    // ===================== ARCHIVI OPERATIONS =====================

    /**
     * Saves all archives to JSON file
     */
    public static void salvaArchivi(Map<String, Archivio> archivi) {
        ensureDataDirectory();
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(ARCHIVI_FILE), StandardCharsets.UTF_8)) {
            
            // Convert to a list of ArchivioData for clean serialization
            List<ArchivioData> data = new ArrayList<>();
            for (Archivio archivio : archivi.values()) {
                data.add(ArchivioData.fromArchivio(archivio));
            }
            
            gson.toJson(data, writer);
            System.out.println("✓ Archivi salvati in " + ARCHIVI_FILE);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio degli archivi: " + e.getMessage());
        }
    }

    /**
     * Loads all archives from JSON file
     */
    public static Map<String, Archivio> caricaArchivi() {
        Map<String, Archivio> archivi = new HashMap<>();
        File file = new File(ARCHIVI_FILE);
        
        if (!file.exists()) {
            System.out.println("File archivi non trovato. Inizializzazione vuota.");
            return archivi;
        }

        try (Reader reader = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {
            
            Type listType = new TypeToken<List<ArchivioData>>(){}.getType();
            List<ArchivioData> dataList = gson.fromJson(reader, listType);
            
            if (dataList != null) {
                for (ArchivioData data : dataList) {
                    Archivio archivio = data.toArchivio();
                    archivi.put(archivio.getNomeArchivio(), archivio);
                }
            }
            System.out.println("✓ Archivi caricati da " + ARCHIVI_FILE);
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Errore durante il caricamento degli archivi: " + e.getMessage());
        }
        
        return archivi;
    }

    // ===================== SOGGETTI OPERATIONS =====================

    /**
     * Saves all subjects to JSON file
     */
    public static void salvaSoggetti(Collection<Soggetto> soggetti) {
        ensureDataDirectory();
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(SOGGETTI_FILE), StandardCharsets.UTF_8)) {
            
            gson.toJson(soggetti, writer);
            System.out.println("✓ Soggetti salvati in " + SOGGETTI_FILE);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dei soggetti: " + e.getMessage());
        }
    }

    /**
     * Loads all subjects from JSON file
     */
    public static List<Soggetto> caricaSoggetti() {
        List<Soggetto> soggetti = new ArrayList<>();
        File file = new File(SOGGETTI_FILE);
        
        if (!file.exists()) {
            System.out.println("File soggetti non trovato. Inizializzazione vuota.");
            return soggetti;
        }

        try (Reader reader = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {
            
            Type listType = new TypeToken<List<Soggetto>>(){}.getType();
            soggetti = gson.fromJson(reader, listType);
            
            if (soggetti == null) {
                soggetti = new ArrayList<>();
            }
            System.out.println("✓ Soggetti caricati da " + SOGGETTI_FILE);
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Errore durante il caricamento dei soggetti: " + e.getMessage());
        } catch (JsonParseException e) {
            System.err.println("Errore nel formato JSON dei soggetti (campo 'type' mancante?): " + e.getMessage());
            System.err.println("Resetting soggetti.json to empty list.");
            // Reset the file to empty array to prevent future crashes
            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream(file), StandardCharsets.UTF_8)) {
                writer.write("[]");
            } catch (IOException ex) {
                System.err.println("Errore durante il reset del file: " + ex.getMessage());
            }
        }
        
        return soggetti;
    }

    // ===================== INNER DATA CLASSES =====================

    /**
     * Data transfer object for Archivio serialization
     */
    public static class ArchivioData {
        public String nomeArchivio;
        public ResponsabileData responsabile;
        public List<Fotografia> fotografie;

        public static ArchivioData fromArchivio(Archivio archivio) {
            ArchivioData data = new ArchivioData();
            data.nomeArchivio = archivio.getNomeArchivio();
            // Get responsabile info from the getter
            Responsabile resp = archivio.getResponsabile();
            if (resp != null) {
                data.responsabile = new ResponsabileData();
                data.responsabile.nome = resp.getNome();
                data.responsabile.indirizzo = resp.getIndirizzo();
                data.responsabile.telefono = resp.getTelefono();
                data.responsabile.orarioApertura = resp.getOrarioApertura();
            }
            data.fotografie = archivio.getFotografie();
            return data;
        }

        public Archivio toArchivio() {
            Responsabile resp = responsabile != null ? responsabile.toResponsabile() 
                    : new Responsabile("Unknown", "", "", "");
            Archivio archivio = new Archivio(nomeArchivio, resp);
            if (fotografie != null) {
                for (Fotografia foto : fotografie) {
                    archivio.aggiungiFoto(foto);
                }
            }
            return archivio;
        }
    }

    /**
     * Data transfer object for Responsabile serialization
     */
    public static class ResponsabileData {
        public String nome;
        public String indirizzo;
        public String telefono;
        public String orarioApertura;

        public Responsabile toResponsabile() {
            return new Responsabile(nome, indirizzo, telefono, orarioApertura);
        }
    }
}
