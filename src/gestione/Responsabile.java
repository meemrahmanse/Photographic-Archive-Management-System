// Responsabile.java - meem

package gestione;


// this class represents a responsible person managing an archive with their context details.
public class Responsabile {

    private String nome;
    private String indirizzo;
    private String telefono;
    private String orarioApertura;


    public Responsabile(String nome, String indirizzo, String telefono, String orarioApertura) {

        this.nome = nome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.orarioApertura = orarioApertura;
    }

     public Responsabile() {}

    // Getters and Setters
    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getIndirizzo() {
        return indirizzo;
    }


    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }


    public String getTelefono() {
        return telefono;
    }


    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getOrarioApertura() {
        return orarioApertura;
    }


    public void setOrarioApertura(String orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    
    @Override
    public String toString() {
        return "Responsabile: " + nome + ", Indirizzo: " + indirizzo + ", Tel: " + telefono + ", Orari: " + orarioApertura;
    }
}



// this class represents a responsible person managing an archive, with their contact details.
