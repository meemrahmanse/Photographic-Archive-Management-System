package app;

import model.GestoreArchivi;
import view.ArchivioFrame;

public class MainApp {
    public static void main(String[] args) {
        GestoreArchivi gestore = GestoreArchivi.getInstance();
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ArchivioFrame(gestore);
        });
    }
}