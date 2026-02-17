package org.sporty.feed.publisher;

import org.sporty.feed.domain.StandardMessage;

public interface MessagePublisher {
    public void publish(StandardMessage message);
}
