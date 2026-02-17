package org.sporty.feed.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sporty.feed.domain.StandardMessage;
import org.springframework.stereotype.Component;

@Component
public class MockMessagePublisher implements MessagePublisher {
    private static final Logger log =
            LoggerFactory.getLogger(MockMessagePublisher.class);

    @Override
    public void publish(StandardMessage message) {
        log.info("Publishing message: {}", message);
    }
}
