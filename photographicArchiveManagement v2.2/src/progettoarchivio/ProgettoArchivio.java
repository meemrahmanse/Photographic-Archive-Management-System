
// ProgettoArchivio.java - meem
// Imports thenexternal classes used in this file 
package progettoarchivio;

import gestione.Archivio; // a single photo archive (a collection of photos)
import gestione.FotoAColore; // a  “color photo” subclass
import gestione.Fotografia; // base class for photographs
import gestione.GestoreArchivi; // manager that holds many archives (singleton)
import gestione.Responsabile; // person responsible for an archive
import java.util.NoSuchElementException; // exception for missing elements like used when a subject is not found
import java.util.Scanner; // reads user input from the console

// main program class 
public class ProgettoArchivio {

    // Initialize the archive manager as a singleton instance
    private static GestoreArchivi gestore = GestoreArchivi.getInstance(); // getInstance() gives access to that one
                                                                          // object.
    // Catalog for managing the subjects (soggetti), like people, places,
    // Politician, objects.
    private static CatalogoSoggetti catalogoSoggetti = CatalogoSoggetti.getInstance();
    // Scanner for taking user input
    private static Scanner scanner = new Scanner(System.in);

    // main method - entry point of the program
    public static void main(String[] args) {
        // Dati di esempio
        try {
            catalogoSoggetti.aggiungiSoggetto(new Personaggio("p1", "Mario Rossi", 'M', false, 1980));
            catalogoSoggetti
                    .aggiungiSoggetto(new Politico("p2", "Luigi Verdi", 'M', false, 1970, "Centro", "Ministro"));
            catalogoSoggetti.aggiungiSoggetto(new Artista("p3", "Anna Bianchi", 'F', true, 1950, "Pittura"));
            catalogoSoggetti.aggiungiSoggetto(new Luogo("l1", "Colosseo", "Anfiteatro romano a Roma"));
            catalogoSoggetti.aggiungiSoggetto(new Oggetto("o1", "Vaso Ming", "Antico vaso cinese"));
        } catch (IllegalArgumentException e) { // exception handling
            // Ignora se i dati di esempio esistono già
        }

        int scelta = -1; // choice variable for menu selection

        // Main menu loop
        while (scelta != 0) {
            System.out.println("\n--- MENU GESTIONE ARCHIVIO FOTOGRAFICO ---");
            System.out.println("1. Gestione Archivi");
            System.out.println("2. Gestione Fotografie");
            System.out.println("3. Visualizza Catalogo Soggetti");
            System.out.println("4. Salva ed esci");
            System.out.println("0. Esci senza salvare");
            System.out.print("Scelta: ");

            try {
                scelta = Integer.parseInt(scanner.nextLine());

                switch (scelta) {
                    case 1:
                        menuGestioneArchivi(); // call to archive management menu
                        break;
                    case 2:
                        menuGestioneFotografie(); // call to photograph management menu
                        break;
                    case 3:
                        visualizzaCatalogo(); // display the subj catalog
                        break;
                    case 4:
                        gestore.salvaSuFile(); // save archives to JSON files
                        catalogoSoggetti.salvaSuFile(); // save subjects to JSON files
                        scelta = 0; // exit
                        break;
                    case 0:
                        System.out.println("Uscita senza salvare.");
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            } catch (NumberFormatException e) { // exception handling
                System.out.println("Inserire un numero valido.");
            }
        }

        scanner.close(); // this function closes the scanner to free up resources.
        System.out.println("Programma terminato.");
    }

    // this is for managing achieves
    private static void menuGestioneArchivi() {
        int scelta = -1;
        while (scelta != 0) {
            System.out.println("\n--- Gestione Archivi ---");
            System.out.println("1. Aggiungi un nuovo archivio");
            System.out.println("2. Visualizza tutti gli archivi");
            System.out.println("3. Elimina un archivio");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            try {
                scelta = Integer.parseInt(scanner.nextLine());
                switch (scelta) {
                    case 1:
                        aggiungiArchivio(); // call to add a new archive
                        break;
                    case 2:
                        visualizzaArchivi(); // call to display all archives
                        break;
                    case 3:
                        eliminaArchivio(); // call to delete an archive
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Inserire un numero valido.");
            }
        }
    }

    // this is for collecting info and adding a new archive
    private static void aggiungiArchivio() {
        String nomeArchivio;
        while (true) {
            System.out.print("Nome archivio: ");
            nomeArchivio = scanner.nextLine().trim();
            if (nomeArchivio.matches("^[a-zA-Z0-9_ -]{3,50}$")) {
                if (gestore.getArchivio(nomeArchivio) == null)
                    break;
                System.out.println("Errore: Archivio già esistente.");
            } else {
                System.out.println("Errore: Il nome deve essere alfanumerico (3-50 caratteri).");
            }
        }

        String nomeResp;
        while (true) {
            System.out.print("Nome responsabile: ");
            nomeResp = scanner.nextLine().trim();
            if (nomeResp.matches("^[a-zA-Z\\s.]{3,50}$"))
                break;
            System.out.println("Errore: Il nome deve contenere solo lettere, spazi o punti (3-50 caratteri).");
        }

        String indirizzo;
        while (true) {
            System.out.print("Indirizzo: ");
            indirizzo = scanner.nextLine().trim();
            if (indirizzo.length() >= 5 && indirizzo.length() <= 100)
                break;
            System.out.println("Errore: Indirizzo non valido (5-100 caratteri).");
        }

        String tel;
        while (true) {
            System.out.print("Telefono: ");
            tel = scanner.nextLine().trim();
            if (tel.matches("^\\+?[0-9\\s\\-\\(\\)]{7,15}$"))
                break;
            System.out.println("Errore: Formato telefono non valido (ammessi numeri, spazi, +, -, parentesi).");
        }

        String orario;
        while (true) {
            System.out.print("Orario apertura: ");
            orario = scanner.nextLine().trim();
            if (orario.length() >= 3 && orario.length() <= 50)
                break;
            System.out.println("Errore: Orario non valido (3-50 caratteri).");
        }

        Responsabile resp = new Responsabile(nomeResp, indirizzo, tel, orario);
        Archivio archivio = new Archivio(nomeArchivio, resp);
        gestore.aggiungiArchivio(archivio); // noted
        System.out.println("Archivio aggiunto con successo!");
    }

    // display all existing archives
    private static void visualizzaArchivi() {
        System.out.println("\n--- Elenco Archivi ---");
        if (gestore.getArchivi().isEmpty()) {
            System.out.println("Nessun archivio presente.");
        } else {
            for (Archivio a : gestore.getArchivi().values()) {
                System.out.println("- " + a.toString());
            }
        }
    }

    // this is for deleting an archive
    private static void eliminaArchivio() {
        System.out.print("Inserisci il nome dell'archivio da eliminare: ");
        String nomeArchivio = scanner.nextLine().trim();

        System.out.print("Sei sicuro di voler eliminare l'archivio e tutte le sue foto? (s/n): ");
        String conferma = scanner.nextLine().trim().toLowerCase();

        if (conferma.equals("s")) {
            boolean rimosso = gestore.rimuoviArchivio(nomeArchivio);
            if (rimosso) {
                System.out.println("Archivio '" + nomeArchivio + "' eliminato con successo.");
            } else {
                System.out.println("Errore: Impossibile trovare l'archivio specificato.");
            }
        } else {
            System.out.println("Operazione annullata.");
        }
    }

    // this for managing photographs
    private static void menuGestioneFotografie() {
        int scelta = -1;
        while (scelta != 0) {
            System.out.println("\n--- Gestione Fotografie ---");
            System.out.println("1. Aggiungi una fotografia");
            System.out.println("2. Cerca una fotografia");
            System.out.println("3. Visualizza foto di un archivio");
            System.out.println("4. Elimina una fotografia");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            try {
                scelta = Integer.parseInt(scanner.nextLine());
                switch (scelta) {
                    case 1:
                        aggiungiFotografia(); // call to add a new photograph
                        break;
                    case 2:
                        cercaFotografia(); // call to search for a photograph
                        break;
                    case 3:
                        visualizzaFotoArchivio(); // call to display photos of an archive
                        break;
                    case 4:
                        eliminaFotografia(); // call to delete a photo
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Inserire un numero valido.");
            }
        }
    }

    /// noted - ei porjonto.

    // adding a new photograph to a selected archive
    private static void aggiungiFotografia() {
        visualizzaArchivi();
        if (gestore.getArchivi().isEmpty()) {
            System.out.println("Nessun archivio presente. Creane uno prima di aggiungere una foto.");
            return;
        }
        System.out.print("Seleziona il nome dell'archivio in cui aggiungere la foto: ");
        String nomeArchivio = scanner.nextLine();
        Archivio archivio = gestore.getArchivio(nomeArchivio);

        if (archivio == null) {
            System.out.println("Archivio non trovato.");
            return;
        }

        String idFoto;
        while (true) {
            System.out.print("ID Foto: ");
            idFoto = scanner.nextLine().trim();
            if (idFoto.matches("^[0-9]+$"))
                break;
            System.out.println("Errore: ID deve essere solo un numero intero.");
        }

        String dim;
        while (true) {
            System.out.print("Dimensione (es. 10x15 o 10x15 cm): ");
            dim = scanner.nextLine().trim();
            if (dim.matches("^[0-9]+[xX][0-9]+\\s*([a-zA-Z]{1,5})?$"))
                break;
            System.out.println("Errore: Formato dimensione non valido (es. 10x15 o 10x15 cm).");
        }

        String stato;
        while (true) {
            System.out.print("Stato di conservazione (Buono, Danneggiato, ...): ");
            stato = scanner.nextLine().trim();
            if (stato.length() >= 3 && stato.length() <= 50)
                break;
            System.out.println("Errore: Lo stato deve avere tra 3 e 50 caratteri.");
        }

        visualizzaCatalogo();

        Soggetto soggetto = null;
        while (soggetto == null) {
            System.out.print("Inserisci la chiave del soggetto da associare: ");
            String chiaveSoggetto = scanner.nextLine().trim();
            try {
                soggetto = catalogoSoggetti.trovaPerChiave(chiaveSoggetto);
            } catch (NoSuchElementException | IllegalArgumentException e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }

        String aColori;
        while (true) {
            System.out.print("La foto è a colori? (s/n): ");
            aColori = scanner.nextLine().trim().toLowerCase();
            if (aColori.equals("s") || aColori.equals("n"))
                break;
            System.out.println("Errore: Rispondi con 's' (sì) o 'n' (no).");
        }

        Fotografia nuovaFoto;
        if (aColori.equals("s")) {
            String tipoStampa;
            while (true) {
                System.out.print("Tipo di stampa (Chiaro/Opaco): ");
                tipoStampa = scanner.nextLine().trim();
                if (tipoStampa.matches("^[a-zA-Z0-9\\s\\-]{3,30}$"))
                    break;
                System.out.println("Errore: Tipo di stampa non valido (3-30 caratteri).");
            }
            nuovaFoto = new FotoAColore(idFoto, dim, stato, soggetto, tipoStampa);
        } else {
            nuovaFoto = new Fotografia(idFoto, dim, stato, soggetto);
        }

        archivio.aggiungiFoto(nuovaFoto);
        System.out.println("Fotografia aggiunta con successo all'archivio " + nomeArchivio);
    }

    private static void cercaFotografia() {
        System.out.print("Inserisci l'ID della fotografia da cercare: ");
        String idFoto = scanner.nextLine();
        boolean trovata = false;

        for (Archivio archivio : gestore.getArchivi().values()) {
            Fotografia foto = archivio.cercaFoto(idFoto);
            if (foto != null) {
                System.out.println("Foto trovata nell'archivio: " + archivio.getNomeArchivio());
                System.out.println(foto.toString());
                trovata = true;
                break;
            }
        }

        if (!trovata) {
            System.out.println("Nessuna fotografia trovata con l'ID: " + idFoto);
        }
    }

    // delete a photograph by ID mapping over all archives or one selected
    private static void eliminaFotografia() {
        System.out.print("Inserisci l'ID della fotografia da eliminare: ");
        String idFoto = scanner.nextLine().trim();

        System.out.print("Sei sicuro di voler eliminare la foto? (s/n): ");
        String conferma = scanner.nextLine().trim().toLowerCase();

        if (conferma.equals("s")) {
            boolean rimossa = false;
            for (Archivio archivio : gestore.getArchivi().values()) {
                if (archivio.rimuoviFoto(idFoto)) {
                    System.out.println("Foto eliminata con successo dall'archivio: " + archivio.getNomeArchivio());
                    rimossa = true;
                    break;
                }
            }
            if (!rimossa) {
                System.out.println("Errore: Impossibile trovare la foto da eliminare.");
            }
        } else {
            System.out.println("Operazione annullata.");
        }
    }

    // show photos of a selected archive
    private static void visualizzaFotoArchivio() {
        visualizzaArchivi();
        if (gestore.getArchivi().isEmpty()) {
            return;
        }
        System.out.print("Seleziona il nome dell'archivio di cui visualizzare le foto: ");
        String nomeArchivio = scanner.nextLine();
        Archivio archivio = gestore.getArchivio(nomeArchivio);

        if (archivio == null) {
            System.out.println("Archivio non trovato.");
            return;
        }

        System.out.println("\n--- Fotografie nell'archivio: " + nomeArchivio + " ---");
        if (archivio.getFotografie().isEmpty()) {
            System.out.println("Nessuna fotografia in questo archivio.");
        } else {
            for (Fotografia f : archivio.getFotografie()) {
                System.out.println("- " + f.toString());
            }
        }
    }

    // display the subject catalog
    private static void visualizzaCatalogo() {
        System.out.println("\n--- Catalogo Soggetti ---");
        if (catalogoSoggetti.dimensione() == 0) {
            System.out.println("Il catalogo è vuoto.");
        } else {
            for (Soggetto s : catalogoSoggetti.tuttiSoggetti()) {
                System.out.println("- " + s.toString());
            }
        }
    }
}
// this is our main class that runs the photographic archive management system,
// providing a console menu for users to manage archives and photographs.