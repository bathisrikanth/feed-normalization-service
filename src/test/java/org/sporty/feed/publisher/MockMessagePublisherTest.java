package org.sporty.feed.publisher;

import org.junit.jupiter.api.Test;
import org.sporty.feed.domain.Provider;
import org.sporty.feed.domain.StandardOddsChange;

import java.util.Map;

import static org.sporty.feed.domain.MarketOutcome.*;

class MockMessagePublisherTest {

    private final MockMessagePublisher publisher = new MockMessagePublisher();

    @Test
    void publish_doesNotThrow() {
        StandardOddsChange message = new StandardOddsChange(
                "ev1",
                Map.of(HOME, 2.0, DRAW, 3.0, AWAY, 4.0),
                Provider.ALPHA
        );
        publisher.publish(message);
    }
}
