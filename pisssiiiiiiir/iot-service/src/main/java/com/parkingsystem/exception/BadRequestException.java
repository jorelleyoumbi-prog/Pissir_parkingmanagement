package com.parkingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a request is invalid.
 * This exception is annotated with @ResponseStatus to automatically return
 * a BAD_REQUEST (400) status code when thrown.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    // campo personalizzato per il codice errore
    private String errorCode;

    public BadRequestException(String message) {
        super(message);
    }

    // Costruttore con messaggio e codice errore
    public BadRequestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // Costruttore con causa
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    // Costruttore con messaggio, causa e codice errore
    public BadRequestException(String message, Throwable cause, String errorCode) {
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
