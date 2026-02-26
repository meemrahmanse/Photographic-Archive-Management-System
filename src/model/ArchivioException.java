package model;

/**
 * Eccezione personalizzata per errori nella gestione degli archivi fotografici.
 * Viene lanciata quando si verificano problemi come archivio non trovato,
 * fotografia duplicata o ID non valido.
 */

public class ArchivioException extends Exception {

	/**
     * Costruisce l'eccezione con un messaggio descrittivo.
     * @param message descrizione dell'errore
     */
	
    public ArchivioException(String message) {
    	
        super(message);
    }

    /**
     * Costruisce l'eccezione con un messaggio e la causa originale.
     * @param message descrizione dell'errore
     * @param cause   eccezione che ha causato questo errore
     */
    
    public ArchivioException(String message, Throwable cause) {
    	
        super(message, cause);
    }
}
