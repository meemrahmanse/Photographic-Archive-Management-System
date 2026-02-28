// JsonDatabase.java - meem
// JSON database utility class using Gson for serialization/deserialization

package database;

import com.google.gson.*;
import gestione.*;
import progettoarchivio.*;
import java.io.*;
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
        } catch (IOException e) {
            // Internal log
        }
    }

    /**
     * Loads all archives from JSON file
     */
    public static Map<String, Archivio> caricaArchivi() {
        Map<String, Archivio> archivi = new HashMap<>();
        File file = new File(ARCHIVI_FILE);

        if (!file.exists()) {
            // File not found, return empty map
            return archivi;
        }

        try (Reader reader = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {

            JsonElement root = JsonParser.parseReader(reader);
            if (root.isJsonArray()) {
                JsonArray array = root.getAsJsonArray();
                for (JsonElement element : array) {
                    try {
                        ArchivioData data = gson.fromJson(element, ArchivioData.class);
                        if (data != null) {
                            Archivio archivio = data.toArchivio();
                            archivi.put(archivio.getNomeArchivio(), archivio);
                        }
                    } catch (Exception e) {
                        // Skip malformed
                    }
                }
            }
        } catch (IOException | JsonParseException e) {
            // Internal log
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
        } catch (IOException e) {
            // Internal log
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

            JsonElement root = JsonParser.parseReader(reader);
            if (root.isJsonArray()) {
                JsonArray array = root.getAsJsonArray();
                for (JsonElement element : array) {
                    try {
                        Soggetto s = gson.fromJson(element, Soggetto.class);
                        if (s != null) {
                            soggetti.add(s);
                        }
                    } catch (Exception e) {
                        // Skip malformed
                    }
                }
            }
        } catch (IOException | JsonParseException e) {
            // Internal log
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
                    archivio.caricaFoto(foto);
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
