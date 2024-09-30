package com.kse.core.authkeycloak.exceptions;


/**
 * La ressource rechercher existe déjà
 */
public class ResourceAlreadyExistsException extends  RuntimeException {

    public ResourceAlreadyExistsException() {
        super();
    }
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
    public ResourceAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
