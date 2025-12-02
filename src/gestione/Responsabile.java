// Responsabile.java - meem

package progettoarchivio;

import java.util.Objects;
// this class represents a responsible person managing an archive with their context details.
public class Responsabile {

    private String nome;
    private String indirizzo;
    private String telefono;
    private String orarioApertura;


    public Responsabile(String nome, String indirizzo, String telefono, String orarioApertura) {

        setNome(nome);
        setIndirizzo(indirizzo);
        setTelefono(telefono);
        setOrarioApertura(orarioApertura);
    }

     public Responsabile() {}

     private void requireNonEmpty(String value, String field) {
         
        if (value == null || value.trim().isEmpty()) {
            
            throw new IllegalArgumentException(field + " non può essere vuoto!");
        }
    }

    private boolean isValidName(String name) {
        
        return name.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s']+$"); // accetta lettere e spazi
    }

    private boolean isValidTelefono(String telefono) {
        
        return telefono.matches("^[0-9+\\-\\s]{5,20}$"); // numeri, +, -, spazi
    }

    private boolean isValidOrario(String orario) {
        
        return orario.matches("^[0-9]{1,2}:[0-9]{2}\\s*-\\s*[0-9]{1,2}:[0-9]{2}$");
    }
        // Getters and Setters
    public String getNome() {
        
        return nome;
    }


    public void setNome(String nome) {
        
        requireNonEmpty(nome, "Nome: ");
        
        if (!isValidName(nome)) {
            
            throw new IllegalArgumentException("Il nome può contenere solo lettere e spazi!");
        }
        this.nome = nome;
    }


    public String getIndirizzo() {
        
        return indirizzo;
    }


    public void setIndirizzo(String indirizzo) {
        
        requireNonEmpty(indirizzo, "Indirizzo: ");
        this.indirizzo = indirizzo.trim();
    }


    public String getTelefono() {
        
        return telefono;
    }


    public void setTelefono(String telefono) {
        
        requireNonEmpty(telefono, "Telefono: ");
        
        if (!isValidTelefono(telefono)) {
            
            throw new IllegalArgumentException("Telefono non valido! Ammessi numeri, spazi, +, -.");
        }
        this.telefono = telefono;
    }


    public String getOrarioApertura() {
        
        return orarioApertura;
    }


    public void setOrarioApertura(String orarioApertura) {
        
        requireNonEmpty(orarioApertura, "Orario di apertura: ");
        if (!isValidOrario(orarioApertura)) {
            
            throw new IllegalArgumentException("Orario non valido! Formato richiesto: HH:MM - HH:MM (es. 09:00 - 17:30)");
        }
        this.orarioApertura = orarioApertura.trim();
    }

    
    @Override
    public String toString() {
        return "Responsabile [Nome: " + nome + ", Indirizzo: " + indirizzo + ", Telefono: " + telefono + ", Orario: " + orarioApertura + "]";
    }
}



// this class represents a responsible person managing an archive, with their contact details.
