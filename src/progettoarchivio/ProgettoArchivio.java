
// ProgettoArchivio.java - meem
// Necessary package and imports
package progettoarchivio;

import java.util.Scanner;   // reads user input from the console



// main program class 
public class ProgettoArchivio {

    // gestorarchivi instance for managing archives, catalogosoggetti instance for managing subjects, and scanner for user input
    private static final GestoreArchivi gestore = GestoreArchivi.getInstance(); 
    private static final CatalogoSoggetti catalogoSoggetti = CatalogoSoggetti.getInstance(); 
    private static final Scanner scanner = new Scanner(System.in);


    // main method - entry point of the program
    public static void main(String[] args) {
        // Dati di esempio
        try {
            
            catalogoSoggetti.aggiungiSoggetto(new Personaggio("p1", "Mario Rossi", 'M', false, 1980));
            catalogoSoggetti.aggiungiSoggetto(new Politico("p2", "Luigi Verdi", 'M', false, 1970, "Centro", "Ministro"));
            catalogoSoggetti.aggiungiSoggetto(new Artista("p3", "Anna Bianchi", 'F', true, 1950, "Pittura"));
            catalogoSoggetti.aggiungiSoggetto(new Luogo("l1", "Colosseo", "Anfiteatro romano a Roma"));
            catalogoSoggetti.aggiungiSoggetto(new Oggetto("o1", "Vaso Ming", "Antico vaso cinese"));
        } catch (IllegalArgumentException ignored) {  // exception handling
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
                System.out.println("Inserire un numero valido!");
            }
        }

        scanner.close();  // this function closes the scanner to free up resources.
        
        System.out.println("Programma terminato.");
        System.out.println("Arrivederci, grazie per aver usato il nostro programma!");
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
        
        String nomeArchivio = String.valueOf(leggiSoloLettere(scanner, "Inserite il nome dell'archivio: "));
        
        String nomeResp = String.valueOf(leggiSoloLettere(scanner, "Inserite il nome del responsabile: "));
        
        System.out.println("Indirizzo: ");
        String indirizzo = scanner.nextLine();
        
        String tel = String.valueOf(leggiNumInt(scanner, "Inserite il numero di telefono: "));
   
        String orario = String.valueOf(leggiNumInt(scanner, "Inserite i orari di apertura: "));

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
            return;
        } 
           for (Archivio a : gestore.getArchivi().values()) {
            System.out.println("- " + a);
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
                        System.out.println("Scelta non valida!");
                }
            } catch (NumberFormatException e) {
                
                System.out.println("Inserite un numero valido!");
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
        
        int altezza = leggiNumInt(scanner, "Altezza della foto: ");
        int larghezza = leggiNumInt(scanner, "Larghezza della foto: ");
        
        if (altezza <= 0 || larghezza <= 0) {
    System.out.println("Dimensioni non valide: inserire solo interi positivi.");
    return;
}
         
        
StatoConservazione statoEnum = null;

      do{ 
      
        System.out.print("Stato di conservazione (Buono, Danneggiato, pessimo, restaurato): ");
        String stato = scanner.nextLine();

        
        try {
            
            statoEnum = StatoConservazione.fromString(stato);
            
        } catch (Exception e) {
            
            System.out.println("Valore non valido per stato di conservazione. Usa Buono / Danneggiato / Pessimo / Restaurato");
            statoEnum = null;
        }
}while (statoEnum == null);
        
        
      visualizzaCatalogo();
      
      Soggetto soggetto = null;

      do {
        System.out.print("Inserisci la chiave del soggetto da associare: ");
        String chiaveSoggetto = scanner.nextLine();
        
        try {
            
            soggetto = catalogoSoggetti.trovaPerChiave(chiaveSoggetto);
            
        }catch (Exception e){
            
            System.out.println("Valore non valido, per favore inserisca una delle chiavi proposte nel catalogo soggetti!");
            
            soggetto = null;
        }
      }while (soggetto == null);

        // ask if the photo is in color
        
        System.out.print("La foto è a colori? (s/n): ");
        String aColori = scanner.nextLine();
        
        Fotografia nuovaFoto;
        
        if (aColori.equalsIgnoreCase("s")) {
            
            System.out.print("Tipo di stampa (Chiaro/Opaco): ");
            String tipoStampa = scanner.nextLine();
            
            nuovaFoto = new FotoAColore(idFoto, altezza, larghezza, statoEnum, soggetto, tipoStampa);
        
        } else {
            
            nuovaFoto = new Fotografia(idFoto, altezza, larghezza, statoEnum, soggetto);
        }

        archivio.aggiungiFoto(nuovaFoto);
        System.out.println("Fotografia aggiunta con successo all'archivio " + nomeArchivio);
      }



    private static void cercaFotografia() {
        System.out.print("Inserisci l'ID della fotografia da cercare: ");
        String idFoto = scanner.nextLine();


        for (Archivio archivio : gestore.getArchivi().values()) {
            Fotografia foto = archivio.cercaFoto(idFoto);
            
            if (foto != null) {
                
                System.out.println("Foto trovata nell'archivio: " + archivio.getNomeArchivio());
                System.out.println(foto.toString());
                return;
            }
        }

        System.out.println("Nessuna foto trovata con l'ID: " + idFoto);
    }
    


    // show photos of a selected archive
    private static void visualizzaFotoArchivio() {
        visualizzaArchivi();
        
        System.out.print("Nome archivio: ");
        String nomeArchivio = scanner.nextLine();
        
        Archivio archivio = gestore.getArchivio(nomeArchivio);
        if (archivio == null) {
            
            System.out.println("Archivio non trovato.");
            return;
        }

        System.out.println("\n--- Fotografie nell'archivio: " + nomeArchivio + " ---");
        
        if(archivio.getFotografie().isEmpty()){
            
            System.out.println("Nessuna fotografia in questo archivio.");
            return;
        } 
            for(Fotografia f : archivio.getFotografie()){
                System.out.println("- " + f);
            }
    }

    // display the subject catalog
    private static void visualizzaCatalogo() {
        
        System.out.println("\n--- Catalogo Soggetti ---");
        
        if (catalogoSoggetti.dimensione() == 0) {
            
            System.out.println("Il catalogo è vuoto.");
            return;
        } 
         for (Soggetto s : catalogoSoggetti.tuttiSoggetti()) {
            System.out.println("- " + s);
        }
    }


//exceptio solo numeri
private static int leggiNumInt(Scanner input, String messaggio) {
    
    int numero = 0;
    boolean valido = false;

    while (!valido) {
        
        System.out.print(messaggio);
        String valore = input.nextLine().trim();

        if (valore.isEmpty()) continue;

        try {
            
            numero = Integer.parseInt(valore);
            valido = true;
            
        } catch (NumberFormatException e) {
            
            System.out.println("\nErrore: inserire solo numeri interi.\n");
        }
    }

    return numero;
}
//exception solo lettere
private static boolean soloLettere(String s) {
    
    return s != null && s.matches("[a-zA-Z]+");
}
private static String leggiSoloLettere(Scanner scanner, String messaggio) {
    
    String input;
    
    do {
        System.out.print(messaggio);
        input = scanner.nextLine();
        
        if (!soloLettere(input)) {
            
            System.out.println("Errore: inserire solo lettere.");
        }
    } while (!soloLettere(input));
    return input;
}
}
// this is our main class that runs the photographic archive management system, providing a console menu for users to manage archives and photographs.
