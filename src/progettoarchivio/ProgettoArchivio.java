
// ProgettoArchivio.java - meem
// Necessary package and imports
package progettoarchivio;

import gestione.Archivio;     //a single photo archive (a collection of photos)
import gestione.FotoAColore;  //a  “color photo” subclass
import gestione.Fotografia;   // base class for photographs
import gestione.GestoreArchivi;  // manager that holds many archives (singleton)
import gestione.Responsabile;    //person responsible for an archive
import java.util.NoSuchElementException; // exception for missing elements like used when a subject is not found
import java.util.Scanner;   // reads user input from the console



// main program class 
public class ProgettoArchivio {

    // gestorarchivi instance for managing archives, catalogosoggetti instance for managing subjects, and scanner for user input
    private static GestoreArchivi gestore = GestoreArchivi.getInstance(); 
    private static CatalogoSoggetti catalogoSoggetti = CatalogoSoggetti.getInstance(); 
    private static Scanner scanner = new Scanner(System.in);


    // main method - entry point of the program
    public static void main(String[] args) {
        // Dati di esempio
        try {
            catalogoSoggetti.aggiungiSoggetto(new Personaggio("p1", "Mario Rossi", 'M', false, 1980));
            catalogoSoggetti.aggiungiSoggetto(new Politico("p2", "Luigi Verdi", 'M', false, 1970, "Centro", "Ministro"));
            catalogoSoggetti.aggiungiSoggetto(new Artista("p3", "Anna Bianchi", 'F', true, 1950, "Pittura"));
            catalogoSoggetti.aggiungiSoggetto(new Luogo("l1", "Colosseo", "Anfiteatro romano a Roma"));
            catalogoSoggetti.aggiungiSoggetto(new Oggetto("o1", "Vaso Ming", "Antico vaso cinese"));
        } catch (IllegalArgumentException e) {  // exception handling
            // Ignora se i dati di esempio esistono già
        }
        int scelta = -1;

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
                        menuGestioneFotografie();  // call to photograph management menu
                        break;
                    case 3:
                        visualizzaCatalogo();   // display the subj catalog
                        break;
                    case 4:
                        gestore.salvaSuFile();   // save archives to files
                        scelta = 0; // Per uscire dal ciclo
                        break;
                    case 0:
                        System.out.println("Uscita senza salvare.");
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            } catch (NumberFormatException e) {         // exception handling
                System.out.println("Inserire un numero valido.");
            }
        }

        scanner.close();  // this function closes the scanner to free up resources.
        System.out.println("Programma terminato.");
    }


    // this is for managing achieves
    private static void menuGestioneArchivi() {
        int scelta = -1;
        while (scelta != 0) {
            System.out.println("\n--- Gestione Archivi ---");
            System.out.println("1. Aggiungi un nuovo archivio");
            System.out.println("2. Visualizza tutti gli archivi");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            try {
                scelta = Integer.parseInt(scanner.nextLine());
                switch (scelta) {
                    case 1:
                        aggiungiArchivio(); //  call to add a new archive
                        break;
                    case 2:
                        visualizzaArchivi(); // call to display all archives
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
        System.out.print("Nome archivio: ");
        String nomeArchivio = scanner.nextLine();
        System.out.print("Nome responsabile: ");
        String nomeResp = scanner.nextLine();
        System.out.print("Indirizzo: ");
        String indirizzo = scanner.nextLine();
        System.out.print("Telefono: ");
        String tel = scanner.nextLine();
        System.out.print("Orario apertura: ");
        String orario = scanner.nextLine();

        Responsabile resp = new Responsabile(nomeResp, indirizzo, tel, orario);
        Archivio archivio = new Archivio(nomeArchivio, resp);
        gestore.aggiungiArchivio(archivio);
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


    // this for managing photographs
    private static void menuGestioneFotografie() {
        int scelta = -1;
        while (scelta != 0) {
            System.out.println("\n--- Gestione Fotografie ---");
            System.out.println("1. Aggiungi una fotografia");
            System.out.println("2. Cerca una fotografia");
            System.out.println("3. Visualizza foto di un archivio");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            try {
                scelta = Integer.parseInt(scanner.nextLine());
                switch (scelta) {
                    case 1:
                        aggiungiFotografia();  // call to add a new photograph
                        break;
                    case 2:
                        cercaFotografia();  // call to search for a photograph
                        break;
                    case 3:
                        visualizzaFotoArchivio();  // call to display photos of an archive
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


        System.out.print("ID Foto: ");
        String idFoto = scanner.nextLine();
        System.out.print("Dimensione (es. 10x15): ");
        String dim = scanner.nextLine();
        System.out.print("Stato di conservazione (Buono, Danneggiato, ...): ");
        String stato = scanner.nextLine();

        visualizzaCatalogo();
        System.out.print("Inserisci la chiave del soggetto da associare: ");
        String chiaveSoggetto = scanner.nextLine();
        Soggetto soggetto = null;
        try {
            soggetto = catalogoSoggetti.trovaPerChiave(chiaveSoggetto);
        } catch (NoSuchElementException | IllegalArgumentException e) {  // exception handling
            System.out.println("Errore: " + e.getMessage());
            return;
        }


        // ask if the photo is in color
        System.out.print("La foto è a colori? (s/n): ");
        String aColori = scanner.nextLine();

        Fotografia nuovaFoto;
        if (aColori.equalsIgnoreCase("s")) {
            System.out.print("Tipo di stampa (Chiaro/Opaco): ");
            String tipoStampa = scanner.nextLine();
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
        if(archivio.getFotografie().isEmpty()){
            System.out.println("Nessuna fotografia in questo archivio.");
        } else {
            for(Fotografia f : archivio.getFotografie()){
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
// this is our main class that runs the photographic archive management system, providing a console menu for users to manage archives and photographs.