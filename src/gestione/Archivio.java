// Archivio.java - meem

package gestione;

import java.util.ArrayList;
import java.util.List;


public class Archivio {

    private String nomeArchivio;
    private Responsabile responsabile;
    private List<Fotografia> fotografie;

    public Archivio(String nomeArchivio, Responsabile responsabile) {

        this.nomeArchivio = nomeArchivio;
        this.responsabile = responsabile;
        this.fotografie = new ArrayList<>();
    }

    public Archivio() {
        this.fotografie = new ArrayList<>();
    }

    public void aggiungiFoto(Fotografia foto) {

        this.fotografie.add(foto);
    }


    public boolean rimuoviFoto(String idFoto) {
        return this.fotografie.removeIf(foto -> foto.getIdFoto().equals(idFoto));
    }



    public Fotografia cercaFoto(String idFoto) {
        for (Fotografia foto : fotografie) {
            if (foto.getIdFoto().equals(idFoto)) {
                return foto;
            }
        }
        return null;
    }

    public String getNomeArchivio() {
        return nomeArchivio;
    }

    public Responsabile getResponsabile() {
        return responsabile;
    }
    
    public List<Fotografia> getFotografie() {
        return fotografie;
    }

    
    @Override
    public String toString() {
        return "Archivio: " + nomeArchivio + " - " + responsabile;
    }
}



// this class represents an archive that contains photographs and is managed by a responsible person.
