package com.parkingsystem.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user is unauthorized to perform an action.
 * This exception is annotated with @ResponseStatus to automatically return
 * a FORBIDDEN (403) status code when thrown.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // Campo per il codice errore
    private String errorCode;

    public UnauthorizedException(String message) {
        super(message);
    }

    // Costruttore con messaggio e codice errore
    public UnauthorizedException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // Costruttore con messaggio e causa
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    // Costruttore con messaggio, causa e codice errore
    public UnauthorizedException(String message, Throwable cause, String errorCode) {
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
