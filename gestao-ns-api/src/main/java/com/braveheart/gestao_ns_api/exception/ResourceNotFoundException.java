package com.braveheart.gestao_ns_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when a specific resource is not found in the system.
 *
 * The @ResponseStatus annotation tells Spring to automatically return a 404 NOT FOUND
 * status code whenever this exception is thrown from a controller.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
