package org.sporty.feed.exception;

/**
 * Thrown when the request payload cannot be parsed (e.g. invalid JSON).
 * Mapped to HTTP 400 Bad Request by the exception handler.
 */
public class InvalidPayloadException extends RuntimeException {

    public InvalidPayloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
