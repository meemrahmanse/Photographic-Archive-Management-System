// Archivio.java - meem

package gestione;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// this class represents an archive, that has a name, a responsible person, and a list of photographs.
public class Archivio implements Serializable {

    private String nomeArchivio;
    private Responsabile responsabile;
    private List<Fotografia> fotografie;

    // constructor
    public Archivio(String nomeArchivio, Responsabile responsabile) {

        this.nomeArchivio = nomeArchivio;
        this.responsabile = responsabile;
        this.fotografie = new ArrayList<>();
    }

    // method for add photo to archive
    public void aggiungiFoto(Fotografia foto) {
        this.fotografie.add(foto);
        GestoreArchivi.getInstance().salvaSuFile(); // Automatic save
    }

    /**
     * Internal method to add photos during JSON loading.
     * Does NOT trigger automatic save to prevent recursion.
     */
    public void caricaFoto(Fotografia foto) {
        this.fotografie.add(foto);
    }

    // for remove photo from archive by #id
    public boolean rimuoviFoto(String idFoto) {
        boolean removed = this.fotografie.removeIf(foto -> foto.getIdFoto().equals(idFoto));
        if (removed) {
            GestoreArchivi.getInstance().salvaSuFile(); // Automatic save
        }
        return removed;
    }

    // method for search photo by #id
    public Fotografia cercaFoto(String idFoto) {
        for (Fotografia foto : fotografie) {
            if (foto.getIdFoto().equals(idFoto)) {
                return foto;
            }
        }
        return null;
    }

    // getter for archive name
    public String getNomeArchivio() {
        return nomeArchivio;
    }

    // getter for photographs list
    public List<Fotografia> getFotografie() {
        return fotografie;
    }

    // getter for responsible person
    public Responsabile getResponsabile() {
        return responsabile;
    }

    @Override
    public String toString() {
        return "Archivio: " + nomeArchivio + " - " + responsabile.toString();
    }
}

// this class represents an archive that contains photographs and is managed by
// a responsible person.