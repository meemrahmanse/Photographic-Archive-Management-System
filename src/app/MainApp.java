package app;

import model.GestoreArchivi;
import view.ArchivioFrame; // il tuo JFrame principale

public class MainApp {

    public static void main(String[] args) {

        // ottieni il singleton
        GestoreArchivi gestore = GestoreArchivi.getInstance();

        // avvia il JFrame passando il gestore
        new ArchivioFrame(gestore); // <- qui la GUI si occuperà di creare JTable + TableModel
    }
}