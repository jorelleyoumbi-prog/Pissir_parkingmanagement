package com.parkingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a resource is not found.
 * This exception is annotated with @ResponseStatus to automatically return
 * a NOT_FOUND (404) status code when thrown.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // Campo per il codice errore
    private String errorCode;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Costruttore con messaggio e codice errore
    public ResourceNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // Costruttore con messaggio e causa
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Costruttore con messaggio, causa e codice errore
    public ResourceNotFoundException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    // Getter per il codice errore
    public String getErrorCode() {
        return errorCode;
    }

    // Setter per il codice errore
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
