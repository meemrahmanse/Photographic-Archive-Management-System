// ProgettoArchivio.java - Improved UI and Validation
package progettoarchivio;

import java.util.Scanner;

public class ProgettoArchivio {

    private static final GestoreArchivi gestore = GestoreArchivi.getInstance(); 
    private static final CatalogoSoggetti catalogoSoggetti = CatalogoSoggetti.getInstance(); 
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Caricamento dati di esempio
        inizializzaDatiEsempio();

        int scelta = -1;

        while (scelta != 0) {
            stampaIntestazione("MENU PRINCIPALE");
            System.out.println("1. Gestione Archivi");
            System.out.println("2. Gestione Fotografie");
            System.out.println("3. Visualizza Catalogo Soggetti");
            System.out.println("4. Salva ed esci");
            System.out.println("0. Esci senza salvare");
            
            scelta = leggiIntero("Scelta: ");

            switch (scelta) {
                case 1:
                    menuGestioneArchivi();
                    break;
                case 2:
                    menuGestioneFotografie();
                    break;
                case 3:
                    visualizzaCatalogo();
                    break;
                case 4:
                    System.out.println("Salvataggio in corso...");
                    gestore.salvaSuFile();
                    System.out.println("Salvataggio completato.");
                    scelta = 0; 
                    break;
                case 0:
                    System.out.println("Uscita senza salvare.");
                    break;
                default:
                    System.out.println(">> Scelta non valida. Riprova.");
            }
        }

        scanner.close();
        System.out.println("Programma terminato.");
    }

    private static void inizializzaDatiEsempio() {
        try {
            catalogoSoggetti.aggiungiSoggetto(new Personaggio("p1", "Mario Rossi", 'M', false, 1980));
            catalogoSoggetti.aggiungiSoggetto(new Politico("p2", "Luigi Verdi", 'M', false, 1970, "Centro", "Ministro"));
            catalogoSoggetti.aggiungiSoggetto(new Artista("p3", "Anna Bianchi", 'F', true, 1950, "Pittura"));
            catalogoSoggetti.aggiungiSoggetto(new Luogo("l1", "Colosseo", "Anfiteatro romano a Roma"));
            catalogoSoggetti.aggiungiSoggetto(new Oggetto("o1", "Vaso Ming", "Antico vaso cinese"));
        } catch (IllegalArgumentException ignored) {
            // Dati già presenti
        }
    }

    // --- GESTIONE ARCHIVI ---

    private static void menuGestioneArchivi() {
        int scelta = -1;
        while (scelta != 0) {
            stampaIntestazione("GESTIONE ARCHIVI");
            System.out.println("1. Aggiungi un nuovo archivio");
            System.out.println("2. Visualizza tutti gli archivi");
            System.out.println("0. Torna al menu principale");
            
            scelta = leggiIntero("Scelta: ");
            
            switch (scelta) {
                case 1:
                    aggiungiArchivio();
                    break;
                case 2:
                    visualizzaArchivi();
                    break;
                case 0:
                    break;
                default:
                    System.out.println(">> Scelta non valida.");
            }
        }
    }

    private static void aggiungiArchivio() {
        System.out.println("\n--- Nuovo Archivio ---");
        String nomeArchivio = leggiStringaNonVuota("Nome archivio: ");
        
        // Controllo duplicati
        if (gestore.getArchivio(nomeArchivio) != null) {
            System.out.println(">> Errore: Esiste già un archivio con questo nome.");
            return;
        }

        String nomeResp = leggiStringaNonVuota("Nome responsabile: ");
        String indirizzo = leggiStringaNonVuota("Indirizzo: ");
        String tel = leggiStringaNonVuota("Telefono: ");
        String orario = leggiStringaNonVuota("Orario apertura: ");

        Responsabile resp = new Responsabile(nomeResp, indirizzo, tel, orario);
        Archivio archivio = new Archivio(nomeArchivio, resp);
        
        gestore.aggiungiArchivio(archivio);
        System.out.println(">> Archivio aggiunto con successo!");
    }

    private static void visualizzaArchivi() {
        stampaIntestazione("ELENCO ARCHIVI");
        if (gestore.getArchivi().isEmpty()) {
            System.out.println(">> Nessun archivio presente.");
        } else {
            for (Archivio a : gestore.getArchivi().values()) {
                System.out.println("- " + a);
            }
        }
    }

    // --- GESTIONE FOTOGRAFIE ---

    private static void menuGestioneFotografie() {
        int scelta = -1;
        while (scelta != 0) {
            stampaIntestazione("GESTIONE FOTOGRAFIE");
            System.out.println("1. Aggiungi una fotografia");
            System.out.println("2. Cerca una fotografia");
            System.out.println("3. Visualizza foto di un archivio");
            System.out.println("0. Torna al menu principale");
            
            scelta = leggiIntero("Scelta: ");
            
            switch (scelta) {
                case 1:
                    aggiungiFotografia();
                    break;
                case 2:
                    cercaFotografia();
                    break;
                case 3:
                    visualizzaFotoArchivio();
                    break;
                case 0:
                    break;
                default:
                    System.out.println(">> Scelta non valida.");
            }
        }
    }

    private static void aggiungiFotografia() {
        if (gestore.getArchivi().isEmpty()) {
            System.out.println(">> Nessun archivio presente. Creane uno prima.");
            return;
        }

        visualizzaArchivi();
        String nomeArchivio = leggiStringaNonVuota("Nome archivio destinazione: ");
        Archivio archivio = gestore.getArchivio(nomeArchivio);

        if (archivio == null) {
            System.out.println(">> Errore: Archivio non trovato.");
            return;
        }

        System.out.println("\n--- Nuova Fotografia ---");
        String idFoto = leggiStringaNonVuota("ID Foto: ");
        
        // Controllo se ID esiste già in questo archivio
        if (archivio.cercaFoto(idFoto) != null) {
            System.out.println(">> Errore: ID Foto già esistente in questo archivio.");
            return;
        }

        System.out.println("Enter height and width:");
        int height = leggiInteroPositivo("Height: ");
        int width = leggiInteroPositivo("Width: ");
        int dim = height * width;
        
        // Gestione sicura dell'Enum
        StatoConservazione statoEnum = null;
        while (statoEnum == null) {
            System.out.print("Stato di conservazione (OTTIMO, BUONO, MEDIOCRE, PESSIMO): ");
            String inputStato = scanner.nextLine().toUpperCase().trim();
            try {
                statoEnum = StatoConservazione.valueOf(inputStato);
            } catch (IllegalArgumentException e) {
                System.out.println(">> Stato non valido. Riprova.");
            }
        }

        visualizzaCatalogo();
        Soggetto soggetto = null;
        while (soggetto == null) {
            String chiaveSoggetto = leggiStringaNonVuota("Inserisci la chiave del soggetto da associare: ");
            soggetto = catalogoSoggetti.trovaPerChiave(chiaveSoggetto);
            if (soggetto == null) {
                System.out.println(">> Soggetto non trovato. Riprova.");
                // Opzionale: permettere di uscire se non si trova il soggetto
                System.out.print("Vuoi riprovare? (s/n): ");
                if (scanner.nextLine().equalsIgnoreCase("n")) return;
            }
        }

        System.out.print("La foto è a colori? (s/n): ");
        String aColori = scanner.nextLine();

        Fotografia nuovaFoto;
        if (aColori.equalsIgnoreCase("s")) {
            String tipoStampa = leggiStringaNonVuota("Tipo di stampa (es. Chiaro/Opaco): ");
            // Nota: Assumo che FotoAColore accetti String per dimensione e stato come nel codice originale,
            // ma passo i valori validati.
            nuovaFoto = new FotoAColore(idFoto, String.valueOf(dim), statoEnum.toString(), soggetto, tipoStampa);
        } else {
            nuovaFoto = new Fotografia(idFoto, dim, statoEnum, soggetto);
        }

        archivio.aggiungiFoto(nuovaFoto);
        System.out.println(">> Fotografia aggiunta con successo all'archivio " + nomeArchivio);
    }

    private static void cercaFotografia() {
        String idFoto = leggiStringaNonVuota("Inserisci l'ID della fotografia da cercare: ");
        boolean trovata = false;

        for (Archivio archivio : gestore.getArchivi().values()) {
            Fotografia foto = archivio.cercaFoto(idFoto);
            if (foto != null) {
                System.out.println(">> Foto trovata nell'archivio: " + archivio.getNomeArchivio());
                System.out.println(foto.toString());
                trovata = true;
                // Non facciamo return qui per mostrare se esistono duplicati in archivi diversi (se permesso)
                // o rimuovere il commento sotto per fermarsi alla prima occorrenza
                // return; 
            }
        }

        if (!trovata) {
            System.out.println(">> Nessuna foto trovata con ID: " + idFoto);
        }
    }

    private static void visualizzaFotoArchivio() {
        visualizzaArchivi();
        String nomeArchivio = leggiStringaNonVuota("Nome archivio: ");
        
        Archivio archivio = gestore.getArchivio(nomeArchivio);
        if (archivio == null) {
            System.out.println(">> Archivio non trovato.");
            return;
        }

        stampaIntestazione("FOTO IN: " + nomeArchivio);
        if (archivio.getFotografie().isEmpty()) {
            System.out.println(">> Nessuna fotografia in questo archivio.");
        } else {
            for (Fotografia f : archivio.getFotografie()) {
                System.out.println("- " + f);
            }
        }
    }

    private static void visualizzaCatalogo() {
        stampaIntestazione("CATALOGO SOGGETTI");
        if (catalogoSoggetti.dimensione() == 0) {
            System.out.println(">> Il catalogo è vuoto.");
        } else {
            for (Soggetto s : catalogoSoggetti.tuttiSoggetti()) {
                System.out.println("- " + s);
            }
        }
    }

    // --- METODI DI UTILITÀ PER INPUT E UI ---

    private static void stampaIntestazione(String titolo) {
        System.out.println("\n=========================================");
        System.out.println("   " + titolo.toUpperCase());
        System.out.println("=========================================");
    }

    private static int leggiIntero(String messaggio) {
        while (true) {
            System.out.print(messaggio);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(">> Errore: Inserire un numero intero valido.");
            }
        }
    }

    private static int leggiInteroPositivo(String messaggio) {
        int num;
        do {
            num = leggiIntero(messaggio);
            if (num <= 0) System.out.println(">> Errore: Il numero deve essere positivo.");
        } while (num <= 0);
        return num;
    }

    private static String leggiStringaNonVuota(String messaggio) {
        String input;
        do {
            System.out.print(messaggio);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) System.out.println(">> Errore: Il campo non può essere vuoto.");
        } while (input.isEmpty());
        return input;
    }
}
