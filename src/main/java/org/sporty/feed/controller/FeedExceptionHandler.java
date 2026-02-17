package org.sporty.feed.controller;

import org.sporty.feed.exception.InvalidPayloadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class FeedExceptionHandler {
private static final Logger log =
            LoggerFactory.getLogger(FeedExceptionHandler.class);

    @ExceptionHandler({InvalidPayloadException.class, IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<Void> handleClientErrors(Exception e) {
        log.error("error message: {}", e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleServerErrors(RuntimeException e) {
        log.error("error message: {}", e);
        return ResponseEntity.internalServerError().build();
    }
}
