package org.sporty.feed.controller;

import org.sporty.feed.exception.InvalidPayloadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeedExceptionHandler {

    @ExceptionHandler({InvalidPayloadException.class, IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<Void> handleClientErrors(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleServerErrors(RuntimeException e) {
        return ResponseEntity.internalServerError().build();
    }
}
