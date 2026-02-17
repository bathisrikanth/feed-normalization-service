package org.sporty.feed.domain;

public interface StandardMessage {
    String getEventId();
    MessageType getMessageType();
    Provider getProviderId();
}

