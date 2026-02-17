package org.sporty.feed.controller;

import org.sporty.feed.domain.Provider;
import org.sporty.feed.service.FeedProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider-beta")
public class BetaController {

    private final FeedProcessingService service;

    public BetaController(FeedProcessingService service) {
        this.service = service;
    }

    @PostMapping("/feed")
    public ResponseEntity<Void> receive(@RequestBody String payload) {
        service.process(Provider.BETA, payload);
        return ResponseEntity.ok().build();
    }
}

