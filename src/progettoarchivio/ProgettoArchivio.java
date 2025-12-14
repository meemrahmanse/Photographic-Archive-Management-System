
// ProgettoArchivio.java - meem

package progettoarchivio;

import java.util.Scanner;   // reads user input from the console
import gestione.*;           // import all class from gestione package


// main program class 
public class ProgettoArchivio {

    //These are our "Backend Managers." They hold the actual data. We make them static so the whole main program shares the same single copy of the data.
    // like a storage
    private static final GestoreArchivi gestore = GestoreArchivi.getInstance(); 
    private static final CatalogoSoggetti catalogoSoggetti = CatalogoSoggetti.getInstance(); 
    private static final Scanner scanner = new Scanner(System.in);   // For taking user input.


    // main method - entry point of the program
    public static void main(String[] args) {
        // Dati di esempio
        
        caricaDatiEsesmpio();    // load example data
        
        int scelta = -1;   // choice variable   

        // Main menu loop
        while (scelta != 0) {
            System.out.println("\n--- BENVENUTO NEL SISTEMA DI GESTIONE DELL ARCHIVIO FOTOGRAFICO ---");
            System.out.println("1. Gestione Archivi");
            System.out.println("2. Gestione Fotografie");
            System.out.println("3. Gestione Soggetti");
            System.out.println("4. Salva ed esci");
            System.out.println("0. Esci senza salvare");
            System.out.print("Scelta: ");

            try {
                
                scelta = Integer.parseInt(scanner.nextLine());

                switch (scelta) {
                    
                    case 1:
                        menuGestioneArchivi();   // call to archive management menu
                        break;
                        
                    case 2:
                        menuGestioneFotografie();  // call to photograph management menu
                        break;
                        
                    case 3:
                        menuGestioneSoggetti();   // display the subj catalog
                        break;
                        
                    case 4:
                        try{
                        gestore.salvaSuFile();    // save archives to files
                        System.out.println("Dati salvati con successo, arrivederci!");
                        }catch (Exception e){
                            System.out.println("Errore nel salvataggio: " + e.getMessage());
                        }
                        scelta = 0;   // Per uscire dal ciclo
                        break;
                        
                    case 0:
                        System.out.println("Uscita senza salvare... Arrivederci!");
                        break;
                        
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            } catch (NumberFormatException e) {         // exception handling
                System.out.println("Errore: inserire un numero valido!");
            }
        }

        scanner.close();  // this function closes the scanner to free up resources.
    }
    

    //dati esempio
    private static void caricaDatiEsesmpio(){
            try {
            
            catalogoSoggetti.aggiungiSoggetto(new Personaggio("p1", "Mario Rossi", 'M', false, 1980));
            catalogoSoggetti.aggiungiSoggetto(new Politico("p2", "Luigi Verdi", 'M', false, 1970, "Centro", "Ministro"));
            catalogoSoggetti.aggiungiSoggetto(new Artista("p3", "Anna Bianchi", 'F', true, 1950, "Pittura"));
            catalogoSoggetti.aggiungiSoggetto(new Luogo("l1", "Colosseo", "Anfiteatro romano a Roma"));
            catalogoSoggetti.aggiungiSoggetto(new Oggetto("o1", "Vaso Ming", "Antico vaso cinese"));
        } catch (Exception ignored) {  // exception handling
            // Ignora se i dati di esempio esistono già
        }
    }


    // menu archivi
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
                        
                }
            } catch (NumberFormatException e) {
                
                System.out.println("Inserisci un numero valido.");
            }
        }
    }

// ei porjionto complete.    

    // this is for collecting info and adding a new archive
    private static void aggiungiArchivio() {
        
        String nomeArchivio = leggiStringa("Nome archivio: ");
        
        String nomeResp = leggiNome("Inserite il nome del responsabile: ");
        
        String indirizzo = leggiStringa("Indirizzo: ");
        
        String telefono = leggiTelefono("Telefono: ");
        
        String orario = leggiStringa("Orario (es: 9-18): "); 
        
        try{ // create manager object and archive object
        Responsabile resp = new Responsabile(nomeResp, indirizzo, telefono, orario);
        Archivio archivio = new Archivio(nomeArchivio, resp); // create archive object and add it to the manager
        
        // here we add the new archive to the manager(gestore)
        gestore.aggiungiArchivio(archivio);
        System.out.println("Archivio aggiunto con successo!");
    }catch (Exception e){
        System.out.println("Errore: " + e.getMessage());
    }
}


    // display all existing archives - 
    private static void visualizzaArchivi() {
        
        System.out.println("\n--- ELENCO ARCHIVI ---");
        
        // check if there are no archives
        if (gestore.getArchivi().isEmpty()) {
            
            System.out.println("Nessun archivio presente.");
            return;
        } 
        // display all archives by iterating through the values of the map using java Stream API(forEach)
        gestore.getArchivi().values().forEach(archivio -> System.out.println("- " + archivio));   // ---

     

  }
    // this for managing photographs
    private static void menuGestioneFotografie() {
        
        int scelta = -1;
        
        while (scelta != 0) {
            
            System.out.println("\n--- Gestione Fotografie ---");
            System.out.println("1. Aggiungi fotografia");
            System.out.println("2. Cerca fotografia");
            System.out.println("3. Visualizza foto archivio");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            
            try {
                // taking user input as number
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
                }
            } catch (NumberFormatException e) {
                System.out.println("Errore: inserite un numero valido!");
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }



    // adding a new photograph to a selected archive
    private static void aggiungiFotografia() {

        visualizzaArchivi(); // display existing archives
        
        // check if there are no archives
        if (gestore.getArchivi().isEmpty()) { 
            
            System.out.println("Nessun archivio presente. Creane uno prima!");
            return;
        }
        
        // select the archive to which the photo will be added
        String nomeArchivio = leggiStringa("Nome archivio: ");  // LEGGIsTRINGA( TO READ STRING FORM USER, LIKE "ABC  ADD")
        // get the archive object from the manager
        Archivio archivio = gestore.getArchivio(nomeArchivio);

        if (archivio == null) {  // check if the archive exists
            
            System.out.println("Archivio non trovato.");
            return;
        }
        
        // ask for the photo ID
        String idFoto = leggiStringa("ID foto: ");
        
        int altezza = leggiIntero("Altezza: ");
        int larghezza = leggiIntero("Larghezza: ");
        
        // read the conservation status
        StatoConservazione stato = leggiStato();  /// -- 
         
        
      visualizzaCatalogo();
      
      Soggetto soggetto = null;

      while (soggetto == null){                   //--
        String chiaveSoggetto = leggiStringa("Chiave soggetto: ");
        soggetto = catalogoSoggetti.trovaPerChiave(chiaveSoggetto);
        
        if (soggetto == null){
            System.out.println("Soggetto non trovato!");
        }
      }


    /// EI PORJONNTO -- 2ND CODING SEASSION

        // ask if the photo is in color
        
        String colori = leggiStringa("La foto è a colori? (sì/no): ");
        
        Fotografia nuovaFoto;
        
        if (colori.equalsIgnoreCase("sì")) {
            
            String tipoStampa = leggiStringa("Tipo di stampa (Chiaro/Opaco): ");
            
            nuovaFoto = new FotoAColore(idFoto, altezza, larghezza, stato, soggetto, tipoStampa);
        
        } else {
            
            nuovaFoto = new Fotografia(idFoto, altezza, larghezza, stato, soggetto);
        }

        try {
            archivio.aggiungiFoto(nuovaFoto);
            System.out.println("Foto aggiunta!");
        } catch (Exception e) {
            System.out.println("Errore aggiunta foto: " + e.getMessage());
        }
    }


    private static void cercaFotografia() {
        
        String idFoto = leggiStringa("ID foto da cercare: ");


        for (Archivio archivio : gestore.getArchivi().values()) {
            Fotografia foto = archivio.cercaFoto(idFoto);
            
            if (foto != null) {
                
                System.out.println("Foto trovata nell'archivio: " + archivio.getNomeArchivio());
                System.out.println(foto.toString());
                return;
            }
        }

        System.out.println("Foto non trovata!");
    }
    


// show photos of a selected archive
    private static void visualizzaFotoArchivio() {
        //print all archivo to select from
        visualizzaArchivi();

        String nomeArchivio = leggiStringa("Nome archivio: ");
        
        Archivio archivio = gestore.getArchivio(nomeArchivio);
       
        if (archivio == null) {
            
            System.out.println("Archivio non trovato.");
            return;
        }
        
        if (archivio.getFotografie().isEmpty()) {
            
            System.out.println("Nessuna foto.");
            return;
        }
         archivio.getFotografie().forEach(f -> System.out.println("- " + f));
    }




// display the subject catalog
    private static void visualizzaCatalogo() {
        
        System.out.println("\n--- Catalogo Soggetti ---");
        
        if (catalogoSoggetti.dimensione() == 0) {
            
            System.out.println("Il catalogo è vuoto.");
            return;
        } 
         catalogoSoggetti.tuttiSoggetti().forEach(s -> System.out.println("- " + s));
    }

    private static void menuGestioneSoggetti() {
        int scelta = -1;

        while (scelta != 0) {
            System.out.println("\n=== GESTIONE SOGGETTI ===");
            System.out.println("1. Visualizza catalogo");
            System.out.println("2. Aggiungi nuovo soggetto");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");

            try {
                scelta = Integer.parseInt(scanner.nextLine());
                switch (scelta) {
                    case 1 -> visualizzaCatalogo();
                    case 2 -> aggiungiSoggetto();
                }
            } catch (NumberFormatException e) {
                System.out.println("Errore: numero non valido.");
            }
        }
    }

    private static void aggiungiSoggetto() {

        System.out.println("\nTipi soggetto:");
        System.out.println("1. Personaggio");
        System.out.println("2. Politico");
        System.out.println("3. Artista");
        System.out.println("4. Luogo");
        System.out.println("5. Oggetto");

        int tipo = leggiIntero("Scelta: ");

        String id = leggiStringa("ID soggetto: ");
        String nome = leggiNome("Nome: ");

        Soggetto nuovo = null;

        switch (tipo) {
            case 1 -> {
                char sesso = leggiChar("Sesso (M/F): ");
                boolean vivente = leggiBoolean("Vivente? s = si, n = no, chose (s/n): ");
                int anno = leggiIntero("Anno nascita: ");
                nuovo = new Personaggio(id, nome, sesso, vivente, anno);
            }
            case 2 -> {
                char sesso = leggiChar("Sesso (M/F): ");
                boolean vivente = leggiBoolean("Vivente? (s/n): ");
                int anno = leggiIntero("Anno nascita: ");
                String partito = leggiStringa("Partito politico: ");
                String carica = leggiStringa("Carica istituzionale: ");
                nuovo = new Politico(id, nome, sesso, vivente, anno, partito, carica);
            }
            case 3 -> {
                char sesso = leggiChar("Sesso (M/F): ");
                boolean vivente = leggiBoolean("Vivente? (s/n): ");
                int anno = leggiIntero("Anno nascita: ");
                String arte = leggiStringa("Disciplina artistica: ");
                nuovo = new Artista(id, nome, sesso, vivente, anno, arte);
            }
            case 4 -> {
                String descr = leggiStringa("Descrizione luogo: ");
                nuovo = new Luogo(id, nome, descr);
            }
            case 5 -> {
                String descr = leggiStringa("Descrizione oggetto: ");
                nuovo = new Oggetto(id, nome, descr);
            }
            default -> {
                System.out.println("Tipo non valido.");
                return;
            }
        }

        try {
            catalogoSoggetti.aggiungiSoggetto(nuovo);
            System.out.println("Soggetto aggiunto!");
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }



//// from here 
///   string: (Mee  M)
/// 
//exceptions
    
private static String leggiStringa(String msg) {
        String s;
        do {
            System.out.print(msg);
            s = scanner.nextLine().trim();
        } while (s.isEmpty());
        return s;
    }
    


private static int leggiIntero(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Inserire solo numeri interi.");
            }
        }
    }


 private static String leggiNome(String msg) {
        String s;
        do {
            System.out.print(msg);
            s = scanner.nextLine().trim();
            if (!s.matches("[A-Za-zÀ-ÖØ-öø-ÿ' ]+"))
                System.out.println("Errore: usare solo lettere, spazi, apostrofi.");
        } while (!s.matches("[A-Za-zÀ-ÖØ-öø-ÿ' ]+"));
        return s;
    }

    private static String leggiTelefono(String msg) {
        String tel;
        do {
            System.out.print(msg);
            tel = scanner.nextLine().trim();
            if (!tel.matches("[0-9+ ]{5,20}"))
                System.out.println("Telefono non valido.");
        } while (!tel.matches("[0-9+ ]{5,20}"));
        return tel;
    }



    private static char leggiChar(String msg) {
        while (true) {
            System.out.print(msg);
            String s = scanner.nextLine().trim();
            if (s.length() == 1) return s.charAt(0);
            System.out.println("Inserisci un solo carattere.");
        }
    }


    private static boolean leggiBoolean(String msg) {
        while (true) {
            System.out.print(msg);
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("s")) return true;
            if (s.equals("n")) return false;
            System.out.println("Rispondere s / n");
        }
    }

    private static StatoConservazione leggiStato() {
        while (true) {
            String s = leggiStringa("Stato (Buono/Danneggiato/Pessimo/Restaurato): ");
            try {
                return StatoConservazione.fromString(s);
            } catch (Exception e) {
                System.out.println("Valore non valido.");
            }
        }
    }

}

