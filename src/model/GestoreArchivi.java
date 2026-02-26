package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

import java.io.*;

import java.util.HashMap;
import java.util.Map;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * GestoreArchivi è un singleton che gestisce più archivi fotografici.
 * Permette di aggiungere, modificare, eliminare e salvare/caricare archivi da file JSON.
 */

public class GestoreArchivi {

    private static volatile GestoreArchivi instance; //thread-safe singleton
    private Map<String, Archivio> archivi;
    private static final String NOME_FILE = "archivio.json";
    
   
    public List<Fotografia> filtraPerStato(StatoConservazione stato) {

        List<Fotografia> risultato = new ArrayList<>();

        if (stato == null)
            return risultato;

        for (Fotografia f : getTutteLeFotografie()) {
            if (stato.equals(f.getStatoConservazione())) {
                risultato.add(f);
            }
        }

        return risultato;
    }


    public List<Fotografia> cercaPerAutore(String autore) {

        List<Fotografia> risultato = new ArrayList<>();

        if (autore == null || autore.isBlank())
            return risultato;

        String query = autore.trim().toLowerCase();

        for (Fotografia f : getTutteLeFotografie()) {
            if (f.getAutore() != null &&
                f.getAutore().toLowerCase().contains(query)) {
                risultato.add(f);
            }
        }

        return risultato;
    }
    
    public List<Fotografia> cercaPerData(LocalDate data) {

        List<Fotografia> risultato = new ArrayList<>();

        if (data == null)
            return risultato;

        for (Fotografia f : getTutteLeFotografie()) {
            if (data.equals(f.getData())) {
                risultato.add(f);
            }
        }

        return risultato;
    }


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

        if (f == null)
            throw new ArchivioException("Fotografia nulla.");

        Archivio archivio = archivi.get(nomeArchivio);

        if (archivio == null)
            throw new ArchivioException("Archivio non trovato.");

        if (archivio.cercaFoto(f.getIdFoto()) != null)
            throw new ArchivioException("ID già esistente.");

        archivio.aggiungiFoto(f);
    }

    public void save(String percorsoFile) throws ArchivioException {

        if (percorsoFile == null || percorsoFile.isBlank())
            throw new ArchivioException("Percorso file non valido.");

        try (Writer writer = new FileWriter(percorsoFile)) {

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            gson.toJson(archivi, writer);

        } catch (IOException e) {
            throw new ArchivioException("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    
    public void load(String percorsoFile) throws ArchivioException {

        if (percorsoFile == null || percorsoFile.isBlank())
            throw new ArchivioException("Percorso file non valido.");

        File file = new File(percorsoFile);

        if (!file.exists()) {
            archivi = new HashMap<>();
            return;
        }

        try  {
        	
        	Reader reader = new FileReader(file);
            Gson gson = new Gson();

            Type tipo = new TypeToken<Map<String, Archivio>>() {}.getType();

            Map<String, Archivio> caricati = gson.fromJson(reader, tipo);

            if (caricati != null)
                archivi = caricati;
            else
                archivi = new HashMap<>();

        } catch (IOException e) {
        	
        	throw new ArchivioException("Errore durante il caricamento: ", e);

        } catch (JsonSyntaxException e) {
        	
        	throw new ArchivioException("Il file JSON è malformato,", e);    
    
        }
    }
    
    
    public void rimuoviFotografia(String nomeArchivio, String idFoto)
            throws ArchivioException {

        if (idFoto == null || idFoto.isBlank())
            throw new ArchivioException("ID non valido.");

        Archivio archivio = archivi.get(nomeArchivio);

        if (archivio == null)
            throw new ArchivioException("Archivio non trovato.");

        Fotografia foto = archivio.cercaFoto(idFoto);

        if (foto == null)
            throw new ArchivioException("Fotografia non trovata.");

        archivio.rimuoviFoto(idFoto);
    }

     
    public void modificaFotografia(String nomeArchivio, Fotografia aggiornata)
            throws ArchivioException {

        if (aggiornata == null)
            throw new ArchivioException("Fotografia nulla.");

        Archivio archivio = archivi.get(nomeArchivio);

        if (archivio == null)
            throw new ArchivioException("Archivio non trovato.");

        String id = aggiornata.getIdFoto();

        if (id == null || id.isBlank())
            throw new ArchivioException("ID non valido.");

        if (archivio.cercaFoto(id) == null)
            throw new ArchivioException("Fotografia non trovata.");

        archivio.aggiornaFoto(aggiornata);
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