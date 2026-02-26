// Responsabile.java - meem

package gestione;

import java.io.Serializable;


// this class represents a responsible person managing an archive with their context details.
public class Responsabile implements Serializable {

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
    
    
    private void requireNonEmpty(String value, String field) {
        
        if (value == null || value.trim().isEmpty()) {
            
            throw new IllegalArgumentException(field + " non può essere vuoto!");
        }
    }
    
private boolean isValidName(String name) {
        
        return name.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s']+$"); // accetta lettere e spazi
    }

private String normalizeTime(String time) {
	
    time = time.replace('.', ':');
    String[] tParts = time.split(":");
    
    int h, m = 0;
    
    try {
    	
        h = Integer.parseInt(tParts[0].trim());
        
        if (tParts.length > 1) {
        	
            m = Integer.parseInt(tParts[1].trim());
        }
        
    } catch (NumberFormatException e) {
    	
         throw new IllegalArgumentException("Orario contiene caratteri non validi.");
    }

    if (h < 0 || h > 23) throw new IllegalArgumentException("Ore non valide (0-23): " + h);
    if (m < 0 || m > 59) throw new IllegalArgumentException("Minuti non validi (0-59): " + m);

    return String.format("%02d:%02d", h, m);
}

private boolean isValidTelefono(String telefono) {
    return telefono.matches("^[0-9+\\-\\s]+$");
}

private String formatAndValidateOrario(String orario) {
    String[] parts = orario.split("-");
    if (parts.length != 2) {
        throw new IllegalArgumentException("Formato non valido. Usa il formato: 09:00 - 18:00");
    }

    String start = normalizeTime(parts[0].trim());
    String end = normalizeTime(parts[1].trim());

    if (start.compareTo(end) >= 0) {
         throw new IllegalArgumentException("L'orario di chiusura deve essere successivo all'apertura.");
    }

    return start + " - " + end;
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
        this.orarioApertura = formatAndValidateOrario(orarioApertura);
    }
    
    @Override // overrides the toString method / stirng representation
    public String toString() {
        return "Responsabile: " + nome + ", Indirizzo: " + indirizzo + ", Tel: " + telefono + ", Orari: " + orarioApertura;
    }
}

// this class represents a responsible person managing an archive, with their contact details.