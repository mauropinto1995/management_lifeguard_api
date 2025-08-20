package com.braveheart.gestao_ns_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when a schedule creation attempt fails
 * due to a business rule conflict (e.g., duplicate entry based on UNIQUE constraints).
 *
 * The @ResponseStatus annotation tells Spring to automatically return a 409 CONFLICT
 * status code whenever this exception is thrown from a controller.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ScheduleConflictException extends RuntimeException {

    public ScheduleConflictException(String message) {
        super(message);
    }
}
