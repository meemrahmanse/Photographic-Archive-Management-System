# Photographic Archive Management System

Questo è un progetto basato su Java-OOP per la gestione di un archivio fotografico. Il sistema consente la catalogazione delle fotografie, la gestione degli archivi e il tracciamento dei soggetti presenti nelle foto. Questo progetto è stato sviluppato come esercizio accademico di programmazione orientata agli oggetti.

## Panoramica del Progetto

Il sistema è progettato per soddisfare i requisiti di un gestore di archivi fotografici. Fornisce funzionalità per organizzare le fotografie in archivi distinti, gestire il personale responsabile e catalogare una vasta varietà di soggetti, tra cui persone, luoghi, opere d'arte e oggetti. L'architettura principale è costruita su principi di OOP come ereditarietà, polimorfismo ed encapsulamento per creare un sistema modulare ed estensibile.

## Funzionalità

Il progetto è suddiviso in due moduli principali, sviluppati da due contributori differenti.

### Core Archive Management (Studente A)

* **Gestione Archivi**: Creazione e gestione di più archivi tramite la classe `Archivio`.
* **Gestione Fotografie**: Aggiunta, rimozione e ricerca di fotografie (`Fotografia` e `FotoAColore`) all'interno di un archivio.
* **Persistenza dei Dati**: Una classe singleton `GestoreArchivi` gestisce il salvataggio di tutti i dati su disco e il caricamento nell'applicazione, assicurando che non vadano persi tra una sessione e l'altra.
* **Gestione del Personale**: Assegnazione di un `Responsabile` a ciascun archivio per monitorarne la proprietà e i contatti.
* **Flusso Principale dell’Applicazione**: Il punto di ingresso (`ProgettoArchivio`) fornisce un'interfaccia testuale per interagire con le funzionalità del sistema.

### Subject Cataloging (Studente B)

* **Modellazione Dettagliata dei Soggetti**: Un modello dati flessibile e astratto per diversi tipi di soggetti (`Soggetto`).
* **Ricca Gerarchia dei Soggetti**:
    * `Personaggio`: Una persona generica, che può essere specializzata in:
        * `Politico`: Un politico con informazioni su partito e ruolo.
        * `Artista`: Un artista con una specifica attività primaria.
    * `OperaArte`: Rappresenta un'opera d'arte, collegata a un artista e a una località.
    * `Luogo`: Rappresenta un luogo geografico.
    * `Oggetto`: Rappresenta un oggetto inanimato generico.
* **Catalogo Centralizzato**: La classe `CatalogoSoggetti` fornisce un archivio centrale per tutti i soggetti, utilizzando una chiave univoca per una gestione efficiente.

## Come Compilare ed Eseguire

Per compilare ed eseguire il progetto, è necessario avere installato un Java Development Kit (JDK).

1. Apri un terminale o prompt dei comandi nella directory principale del progetto.

2. Compila tutti i file sorgente Java utilizzando il seguente comando:
    ```sh
    javac -d . src/gestione/*.java src/progettoarchivio/*.java
    ```
    Questo comando compila tutti i file `.java` nella directory `src` e colloca i file `.class` compilati nella cartella principale del progetto, rispettando la struttura dei package.

3. Esegui l’applicazione con questo comando:
    ```sh
    java progettoarchivio.ProgettoArchivio
    ```
    Questo avvierà l’applicazione e mostrerà il menu principale nella console.

## Struttura del Progetto

Il codice sorgente è organizzato in due package principali all'interno della directory `src`:

* `src/gestione/`: Contiene tutte le classi relative alla gestione degli archivi, fotografie e personale responsabile (lavoro dello Studente A).
* `src/progettoarchivio/`: Contiene il modello dati dei soggetti e il sistema di catalogazione (principalmente lavoro dello Studente B), oltre alla classe principale `ProgettoArchivio.java` (Studente A).

*Nota: Ci sono file `.java` duplicati nella directory principale. Sembrano versioni più vecchie o non strutturate delle classi presenti in `src/`. Per compilare ed eseguire, vengono usati solo i file nella cartella `src/`.*

## Sviluppi Futuri

Di seguito sono riportati potenziali miglioramenti futuri del progetto:

* **Interfaccia Grafica (GUI)**: Sostituire l'attuale interfaccia testuale con una moderna GUI usando JavaFX, includendo funzionalità come drag-and-drop e anteprime delle immagini.
* **Architettura Client-Server**: Evolvere il sistema in un'applicazione multiutente con un server centrale che gestisce i dati e più client che vi si connettono.
* **Integrazione Cloud**: Aggiungere funzionalità per il backup o la sincronizzazione degli archivi con servizi cloud come Google Drive o AWS S3.
* **Ricerca Avanzata**: Implementare funzionalità di ricerca avanzata (es. ricerca per intervallo di date, attributi dei soggetti, ecc.).

## Contributori

* **Studente A**: Responsabile del sistema di gestione archivi, persistenza dei dati e logica principale dell'applicazione.
* **Studente B**: Responsabile del modello dati dei soggetti e del sistema di catalogazione.
